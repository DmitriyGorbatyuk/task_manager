package ua.khai.gorbatiuk.taskmanager.web.servlet;


import org.apache.log4j.Logger;
import ua.khai.gorbatiuk.taskmanager.entity.bean.LoginUserBean;
import ua.khai.gorbatiuk.taskmanager.util.constant.Attributes;
import ua.khai.gorbatiuk.taskmanager.util.constant.MessageKey;
import ua.khai.gorbatiuk.taskmanager.util.constant.RequestParameter;
import ua.khai.gorbatiuk.taskmanager.util.converter.Converter;
import ua.khai.gorbatiuk.taskmanager.exception.ServiceException;
import ua.khai.gorbatiuk.taskmanager.exception.WrongUserDataException;
import ua.khai.gorbatiuk.taskmanager.entity.model.User;
import ua.khai.gorbatiuk.taskmanager.service.UserService;
import ua.khai.gorbatiuk.taskmanager.util.validator.LoginUserBeanValidator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;


public class LoginServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(LoginServlet.class);

    private static final String USER_BEAN_NAME = "loginUserBean";
    private static final String LOGIN_URI = "/login";
    private static final String TASKS_URI = "/tasks";
    private static final String LOGIN_PAGE = "jsp/login.jsp";

    private UserService userService;
    private LoginUserBeanValidator userBeanValidator;
    private Converter<HttpServletRequest, LoginUserBean> requestToLoginUserBeanConverter;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userService = (UserService) getServletContext().getAttribute(Attributes.USER_SERVICE);
        userBeanValidator = (LoginUserBeanValidator) getServletContext().getAttribute(Attributes.LOGIN_USER_BEAN_VALIDATOR);
        requestToLoginUserBeanConverter = (Converter<HttpServletRequest, LoginUserBean>)
                getServletContext().getAttribute(Attributes.REQUEST_TO_LOGIN_BEAN_CONVERTER);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        replaceAttributesFromSession(request);
        if(request.getSession().getAttribute(RequestParameter.CURRENT_USER) == null) {
            request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
        } else {
            request.getRequestDispatcher(TASKS_URI).forward(request, response);
        }

    }

    private void replaceAttributesFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        putAttributesToRequest(request, session);
        removeExtraAttributes(session);
    }

    private void putAttributesToRequest(HttpServletRequest request, HttpSession session) {
        request.setAttribute(USER_BEAN_NAME, session.getAttribute(USER_BEAN_NAME));
        request.setAttribute(MessageKey.ERRORS_LOGIN, session.getAttribute(MessageKey.ERRORS_LOGIN));
    }

    private void removeExtraAttributes(HttpSession session) {
        session.removeAttribute(USER_BEAN_NAME);
        session.removeAttribute(MessageKey.ERRORS_LOGIN);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LoginUserBean userBean = requestToLoginUserBeanConverter.convert(request);
        Map<String, String> errors = userBeanValidator.validate(userBean);

        User currentUser = tryToLoginUser(userBean, errors);

        putSessionAttributes(currentUser, userBean, errors, request);
        response.sendRedirect(LOGIN_URI);
    }

    private User tryToLoginUser(LoginUserBean userBean, Map<String, String> errors) {
        if (errors.isEmpty()) {
            try {
                return userService.getUserByEmailAndPassword(userBean.getEmail(), userBean.getPassword());
            } catch (WrongUserDataException e) {
                errors.put(MessageKey.ERROR_CANNOT_LOGIN, e.getMessage());
            } catch (ServiceException e) {
                logger.error("Cannot log in user not because of user data", e);
            }
        }
        return null;
    }

    private void putSessionAttributes(User currentUser, LoginUserBean userBean, Map<String, String> errors, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (errors.isEmpty()) {
            addCurrentUserToSession(currentUser, session);
        } else {
            addBeanAndErrorsToSession(userBean, errors, session);
        }
    }

    private void addCurrentUserToSession(User currentUser, HttpSession session) {
        logger.debug("User login: " + currentUser.getEmail() + " is logged in");
        session.setAttribute(RequestParameter.CURRENT_USER, currentUser);
    }

    private void addBeanAndErrorsToSession(LoginUserBean userBean, Map<String, String> errors, HttpSession session) {
        logger.debug("User login: " + userBean.getEmail() + " isn't logged in");
        session.setAttribute(MessageKey.ERRORS_LOGIN, errors);
        userBean.setPassword("");
        session.setAttribute(USER_BEAN_NAME, userBean);
    }
}
