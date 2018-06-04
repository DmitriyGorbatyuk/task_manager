package ua.khai.gorbatiuk.taskmanager.web.listener;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import ua.khai.gorbatiuk.taskmanager.dao.CategoryDao;
import ua.khai.gorbatiuk.taskmanager.dao.TaskDao;
import ua.khai.gorbatiuk.taskmanager.dao.impl.CategoryDaoMysql;
import ua.khai.gorbatiuk.taskmanager.dao.impl.TaskDaoMySql;
import ua.khai.gorbatiuk.taskmanager.entity.model.Category;
import ua.khai.gorbatiuk.taskmanager.entity.model.Task;
import ua.khai.gorbatiuk.taskmanager.service.CategoryService;
import ua.khai.gorbatiuk.taskmanager.service.TaskService;
import ua.khai.gorbatiuk.taskmanager.service.impl.CategoryServiceImpl;
import ua.khai.gorbatiuk.taskmanager.service.impl.TaskServiceImpl;
import ua.khai.gorbatiuk.taskmanager.util.constant.Attributes;
import ua.khai.gorbatiuk.taskmanager.util.converter.Converter;
import ua.khai.gorbatiuk.taskmanager.util.converter.RegistrationUserBeanToUserConverter;
import ua.khai.gorbatiuk.taskmanager.util.converter.populator.Populator;
import ua.khai.gorbatiuk.taskmanager.util.converter.populator.ResultSetToCategoryPopulator;
import ua.khai.gorbatiuk.taskmanager.util.converter.populator.UserToPreparedStatementPopulator;
import ua.khai.gorbatiuk.taskmanager.util.converter.request.RequestToLoginBeanConverter;
import ua.khai.gorbatiuk.taskmanager.util.converter.request.RequestToRegistrationUserBeanConverter;
import ua.khai.gorbatiuk.taskmanager.util.converter.request.RequestToTasksBeanConverter;
import ua.khai.gorbatiuk.taskmanager.util.converter.resultset.ResultSetToCategoryConverter;
import ua.khai.gorbatiuk.taskmanager.util.converter.resultset.ResultSetToTaskConverter;
import ua.khai.gorbatiuk.taskmanager.util.converter.resultset.ResultSetToUserConverter;
import ua.khai.gorbatiuk.taskmanager.dao.UserDao;
import ua.khai.gorbatiuk.taskmanager.dao.connection.ConnectionHolder;
import ua.khai.gorbatiuk.taskmanager.dao.connection.MysqlConnectionManager;
import ua.khai.gorbatiuk.taskmanager.dao.connection.MysqlTransactionManager;
import ua.khai.gorbatiuk.taskmanager.dao.impl.UserDAOMysql;
import ua.khai.gorbatiuk.taskmanager.entity.model.User;
import ua.khai.gorbatiuk.taskmanager.service.UserService;
import ua.khai.gorbatiuk.taskmanager.service.impl.UserServiceImpl;
import ua.khai.gorbatiuk.taskmanager.util.validator.LoginUserBeanValidator;
import ua.khai.gorbatiuk.taskmanager.util.validator.RegistrationUserBeanValidator;
import ua.khai.gorbatiuk.taskmanager.util.validator.secirity.Security;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ContextListener implements ServletContextListener {
    private static final Logger logger = Logger.getLogger(ContextListener.class);

    private RegistrationUserBeanValidator registrationUserBeanValidator;
    private LoginUserBeanValidator loginUserBeanValidator;
    private UserService userService;
    private RegistrationUserBeanToUserConverter registrationUserBeanToUserConverter;
    private RequestToLoginBeanConverter requestToLoginBeanConverter;
    private RequestToRegistrationUserBeanConverter requestToRegistrationUserBeanConverter;
    private RequestToTasksBeanConverter requestToTasksBeanConverter;
    private TaskService taskService;
    private CategoryService categoryService;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        ServletContext servletContext = servletContextEvent.getServletContext();

        initLog4j(servletContext);
        initializeFields(servletContext);
        putAttributesToContext(servletContext);

        logger.debug("contextInitialized finished");
    }

    private void initLog4j(ServletContext servletContext) {
        try {
            PropertyConfigurator.configure(servletContext.getRealPath("WEB-INF/log4j.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeFields(ServletContext servletContext) {

        initializeValidators();
        initializeTransactionServices(servletContext);
        initializeConvertersForServlets();
    }


    private void initializeValidators() {
        Security security = new Security();
        registrationUserBeanValidator = new RegistrationUserBeanValidator(security);
        loginUserBeanValidator = new LoginUserBeanValidator(security);
    }

    private void initializeTransactionServices(ServletContext servletContext) {
        ConnectionHolder connectionHolder = initializeConnectionHolder(servletContext);
        MysqlTransactionManager transactionManager = new MysqlTransactionManager(connectionHolder);

        initializeUserService(transactionManager, connectionHolder);
        initializeTaskService(transactionManager, connectionHolder);
        initializeCategoryService(transactionManager, connectionHolder);
    }

    private ConnectionHolder initializeConnectionHolder(ServletContext servletContext) {
        String dbName = servletContext.getInitParameter(Attributes.DB_NAME);
        MysqlConnectionManager connectionManager = new MysqlConnectionManager(dbName);

        return new ConnectionHolder(connectionManager);
    }

    private void initializeUserService(MysqlTransactionManager transactionManager,
                                       ConnectionHolder connectionHolder) {
        UserDao userDAO = initializeUserDao(connectionHolder);
        userService = new UserServiceImpl(userDAO, transactionManager);
    }

    private UserDao initializeUserDao(ConnectionHolder connectionHolder) {
        Converter<ResultSet, User> converter = new ResultSetToUserConverter();
        Populator<User, PreparedStatement> populator = new UserToPreparedStatementPopulator();

        return new UserDAOMysql(connectionHolder, populator, converter);
    }

    private void initializeTaskService(MysqlTransactionManager transactionManager,
                                       ConnectionHolder connectionHolder) {
        Populator<ResultSet, Category> categoryPopulator = new ResultSetToCategoryPopulator();
        Converter<ResultSet, Task> converter = new ResultSetToTaskConverter(categoryPopulator);
        TaskDao taskDao = new TaskDaoMySql(connectionHolder, converter);
        taskService = new TaskServiceImpl(transactionManager, taskDao);
    }

    private void initializeCategoryService(MysqlTransactionManager transactionManager,
                                           ConnectionHolder connectionHolder) {

        Converter<ResultSet, Category> categoryConverter = new ResultSetToCategoryConverter();
        CategoryDao categoryDao = new CategoryDaoMysql(connectionHolder,categoryConverter);
        categoryService = new CategoryServiceImpl(transactionManager, categoryDao);
    }


    private void initializeConvertersForServlets() {
        registrationUserBeanToUserConverter = new RegistrationUserBeanToUserConverter();
        requestToLoginBeanConverter = new RequestToLoginBeanConverter();
        requestToRegistrationUserBeanConverter = new RequestToRegistrationUserBeanConverter();
        requestToTasksBeanConverter = new RequestToTasksBeanConverter();
    }

    private void putAttributesToContext(ServletContext servletContext) {
        putValidatorsToContext(servletContext);
        putServicesToContext(servletContext);
        putConvertersToContext(servletContext);
    }

    private void putValidatorsToContext(ServletContext servletContext) {
        servletContext.setAttribute(Attributes.REGISTRATION_USER_BEAN_VALIDATOR, registrationUserBeanValidator);
        servletContext.setAttribute(Attributes.LOGIN_USER_BEAN_VALIDATOR, loginUserBeanValidator);
    }

    private void putServicesToContext(ServletContext servletContext) {
        servletContext.setAttribute(Attributes.USER_SERVICE, userService);
        servletContext.setAttribute(Attributes.TASK_SERVICE, taskService);
        servletContext.setAttribute(Attributes.CATEGORY_SERVICE, categoryService);
    }

    private void putConvertersToContext(ServletContext servletContext) {
        servletContext.setAttribute(Attributes.REGISTRATION_BEAN_TO_USER_CONVERTER, registrationUserBeanToUserConverter);
        servletContext.setAttribute(Attributes.REQUEST_TO_LOGIN_BEAN_CONVERTER, requestToLoginBeanConverter);
        servletContext.setAttribute(Attributes.REQUEST_TO_REGISTRATION_BEAN_CONVERTER, requestToRegistrationUserBeanConverter);
        servletContext.setAttribute(Attributes.REQUEST_TO_TASKS_BEAN_CONVERTER,requestToTasksBeanConverter);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        /*do nothing*/
    }
}
