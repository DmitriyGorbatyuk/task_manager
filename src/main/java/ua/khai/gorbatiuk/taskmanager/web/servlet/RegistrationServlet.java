package ua.khai.gorbatiuk.taskmanager.web.servlet;

import org.apache.log4j.Logger;
import ua.khai.gorbatiuk.taskmanager.entity.bean.RegistrationUserBean;
import ua.khai.gorbatiuk.taskmanager.entity.model.Category;
import ua.khai.gorbatiuk.taskmanager.entity.model.Task;
import ua.khai.gorbatiuk.taskmanager.service.CategoryService;
import ua.khai.gorbatiuk.taskmanager.service.TaskService;
import ua.khai.gorbatiuk.taskmanager.util.constant.Attributes;
import ua.khai.gorbatiuk.taskmanager.util.constant.Message;
import ua.khai.gorbatiuk.taskmanager.util.constant.MessageKey;
import ua.khai.gorbatiuk.taskmanager.util.converter.Converter;
import ua.khai.gorbatiuk.taskmanager.exception.ServiceException;
import ua.khai.gorbatiuk.taskmanager.exception.WrongUserDataException;
import ua.khai.gorbatiuk.taskmanager.entity.model.User;
import ua.khai.gorbatiuk.taskmanager.service.UserService;
import ua.khai.gorbatiuk.taskmanager.util.validator.RegistrationUserBeanValidator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

public class RegistrationServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(RegistrationServlet.class);

    private static final String REGISTRATION_URI = "/registration";
    private static final String REGISTRATION_PAGE = "jsp/registration.jsp";
    private static final String USER_BEAN_NAME = "userBean";


    private RegistrationUserBeanValidator registrationUserBeanValidator;
    private Converter<HttpServletRequest, RegistrationUserBean> requestToUserBeanConverter;
    private Converter<RegistrationUserBean, User> registrationUserBeanToUserConverter;
    private UserService userService;
    private TaskService taskService;
    private CategoryService categoryService;
    ;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = getServletContext();
        registrationUserBeanValidator = (RegistrationUserBeanValidator) context.getAttribute(
                Attributes.REGISTRATION_USER_BEAN_VALIDATOR);
        userService = (UserService) context.getAttribute(Attributes.USER_SERVICE);
        taskService = (TaskService) context.getAttribute(Attributes.TASK_SERVICE);
        categoryService = (CategoryService) context.getAttribute(Attributes.CATEGORY_SERVICE);
        requestToUserBeanConverter = (Converter<HttpServletRequest, RegistrationUserBean>)
                context.getAttribute(Attributes.REQUEST_TO_REGISTRATION_BEAN_CONVERTER);
        registrationUserBeanToUserConverter = (Converter<RegistrationUserBean, User>)
                context.getAttribute((Attributes.REGISTRATION_BEAN_TO_USER_CONVERTER));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        replaceAttributesFromSession(request);
        request.getRequestDispatcher(REGISTRATION_PAGE).forward(request, response);
    }

    private void replaceAttributesFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        putAttributesToRequest(request, session);
        removeExtraAttributes(session);
    }

    private void putAttributesToRequest(HttpServletRequest request, HttpSession session) {
        request.setAttribute(USER_BEAN_NAME, session.getAttribute(USER_BEAN_NAME));
        request.setAttribute(MessageKey.ERRORS_REGISTRATION, session.getAttribute(MessageKey.ERRORS_REGISTRATION));
        request.setAttribute(MessageKey.SUCCESS_REGISTRATION, session.getAttribute(MessageKey.SUCCESS_REGISTRATION));
    }

    private void removeExtraAttributes(HttpSession session) {
        session.removeAttribute(USER_BEAN_NAME);
        session.removeAttribute(MessageKey.ERRORS_REGISTRATION);
        session.removeAttribute(MessageKey.SUCCESS_REGISTRATION);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            handleUserAddition(request, response);
        } catch (Exception e) {
            logger.error("Handle user addition does not work", e);
            response.sendRedirect(REGISTRATION_URI);
        }
    }

    private void handleUserAddition(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        RegistrationUserBean bean = requestToUserBeanConverter.convert(request);
        Map<String, String> errors = registrationUserBeanValidator.validate(bean);

        addUserBean(bean, errors, request);

        redirect(request, response, bean, errors);
    }

    private void addUserBean(RegistrationUserBean bean, Map<String, String> errors,
                             HttpServletRequest request) throws IOException, ServletException {

        if (errors.isEmpty()) {
            tryToRegisterUser(registrationUserBeanToUserConverter.convert(bean), errors, request);
        }
    }

    private void tryToRegisterUser(User user, Map<String, String> errors, HttpServletRequest request)
            throws IOException, ServletException {
        try {
            userService.register(user);
        } catch (WrongUserDataException e) {
            errors.put(MessageKey.ERROR_EXISTENCE, e.getMessage());
        } catch (ServiceException e) {
            logger.error("Cannot register user not because of user data", e);
            errors.put(MessageKey.ERROR_EXISTENCE, "Try again later");
        }
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response,
                          RegistrationUserBean bean, Map<String, String> errors) throws IOException {

        HttpSession session = request.getSession();
        if (errors.isEmpty()) {
            setSuccessMessageInSession(bean, session);
            addDefaultData(userService.getUserByEmailAndPassword(bean.getEmail(), bean.getPassword()));
        } else {
            setBeanAndErrorsInSession(bean, errors, session);
        }
        response.sendRedirect(REGISTRATION_URI);
    }

    private void setSuccessMessageInSession(RegistrationUserBean bean, HttpSession session) {
        logger.debug("User email: " + bean.getEmail() + " is registered");
        session.setAttribute(MessageKey.SUCCESS_REGISTRATION, Message.SUCCESS_REGISTRATION);
    }

    private void setBeanAndErrorsInSession(RegistrationUserBean bean, Map<String, String> errors, HttpSession session) {
        logger.debug("User email: " + bean.getEmail() + " isn't registered");

        bean.setPassword("");
        bean.setConfirmedPassword("");

        session.setAttribute(USER_BEAN_NAME, bean);
        session.setAttribute(MessageKey.ERRORS_REGISTRATION, errors);
    }

    private void addDefaultData(User user) {
        Category defaultCategory = new Category();
        defaultCategory.setName("Default category");
        defaultCategory.setColor("#EEEEEE");
        defaultCategory.setRootId(1);
        defaultCategory.setUser(user);
        categoryService.add(defaultCategory);

        Task defaultTask = new Task();
        defaultTask.setCategory(categoryService.getAllByUserId(user.getId()).get(0));
        defaultTask.setName("Default task");
        defaultTask.setRootId(1);
        defaultTask.setUser(user);
        taskService.add(defaultTask);
    }
}
