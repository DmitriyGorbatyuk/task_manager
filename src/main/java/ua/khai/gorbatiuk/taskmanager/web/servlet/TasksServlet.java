package ua.khai.gorbatiuk.taskmanager.web.servlet;

import org.apache.log4j.Logger;
import ua.khai.gorbatiuk.taskmanager.entity.bean.AddTaskBean;
import ua.khai.gorbatiuk.taskmanager.entity.bean.TasksBean;
import ua.khai.gorbatiuk.taskmanager.entity.model.Category;
import ua.khai.gorbatiuk.taskmanager.exception.ConverterException;
import ua.khai.gorbatiuk.taskmanager.exception.CriticalUserDataException;
import ua.khai.gorbatiuk.taskmanager.exception.ServiceException;
import ua.khai.gorbatiuk.taskmanager.exception.WrongUserDataException;
import ua.khai.gorbatiuk.taskmanager.service.CategoryService;
import ua.khai.gorbatiuk.taskmanager.util.constant.Attributes;
import ua.khai.gorbatiuk.taskmanager.entity.model.Task;
import ua.khai.gorbatiuk.taskmanager.service.TaskService;
import ua.khai.gorbatiuk.taskmanager.util.constant.RequestParameter;
import ua.khai.gorbatiuk.taskmanager.util.converter.Converter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class TasksServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(TasksServlet.class);

    private static final String TASKS_PAGE = "jsp/tasks.jsp";
    private static final String NEIGHBORS = "neighbors";
    private static final String CHILDREN = "children";
    private static final String ALL_CATEGORIES = "allCategories";
    private static final int ROOT_TASK_ID = 1;

    private TaskService taskService;
    private CategoryService categoryService;
    private Converter<HttpServletRequest, TasksBean> requestToTasksBeanConverter;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        replaceParametersFromSession(request);
        putTasks(request);

        request.getRequestDispatcher(TASKS_PAGE).forward(request, response);
    }

    private void replaceParametersFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession();

    }

    private void putTasks(HttpServletRequest request) {
        TasksBean tasksBean = requestToTasksBeanConverter.convert(request);

        if (tasksBean.getCurrentTaskId() != ROOT_TASK_ID) {
            tryToPutAllTasks(request, tasksBean);
        } else {
            putNeighbors(request, tasksBean, tasksBean.getCurrentTaskId());
        }
    }

    private void tryToPutAllTasks(HttpServletRequest request, TasksBean tasksBean) {
        try {
            Task currentTask = putCurrentTask(request, tasksBean);
            putNeighbors(request, tasksBean, currentTask.getRootId());
            putChildren(request, tasksBean);
        } catch (WrongUserDataException e) {
            throw new CriticalUserDataException(e.getMessage());
        } catch (ServiceException e) {
            logger.debug(e);
        }
    }

    private void putNeighbors(HttpServletRequest request, TasksBean tasksBean, Integer rootId) {
        List<Task> neighbors = taskService.getAllByUserIdAndRootTaskId(tasksBean.getUserId(), rootId);
        request.setAttribute(NEIGHBORS, neighbors);
    }

    private Task putCurrentTask(HttpServletRequest request, TasksBean tasksBean) {
        Task currentTask = taskService.getByUserIdAndTaskId(tasksBean.getUserId(), tasksBean.getCurrentTaskId());
        request.getSession().setAttribute(RequestParameter.CURRENT_TASK, currentTask);
        putCategories(request);
        return currentTask;
    }

    private void putCategories(HttpServletRequest request) {
        List<Category> categories = categoryService.getAll();
        request.setAttribute(ALL_CATEGORIES, categories);
    }

    private void putChildren(HttpServletRequest request, TasksBean tasksBean) {
        try{
            List<Task> children = taskService.getAllByUserIdAndRootTaskId(tasksBean.getUserId(), tasksBean.getCurrentTaskId());
            request.setAttribute(CHILDREN, children);
        } catch (WrongUserDataException e) {
            /* nothing to do*/
        }

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        taskService = (TaskService) getServletContext().getAttribute(Attributes.TASK_SERVICE);
        categoryService = (CategoryService) getServletContext().getAttribute(Attributes.CATEGORY_SERVICE);
        requestToTasksBeanConverter = (Converter<HttpServletRequest, TasksBean>)
                getServletContext().getAttribute(Attributes.REQUEST_TO_TASKS_BEAN_CONVERTER);
    }
}
