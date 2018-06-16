package ua.khai.gorbatiuk.taskmanager.web.servlet.task;

import org.apache.log4j.Logger;
import ua.khai.gorbatiuk.taskmanager.entity.bean.TasksBean;
import ua.khai.gorbatiuk.taskmanager.entity.model.Category;
import ua.khai.gorbatiuk.taskmanager.entity.model.Task;
import ua.khai.gorbatiuk.taskmanager.exception.CriticalUserDataException;
import ua.khai.gorbatiuk.taskmanager.exception.ServiceException;
import ua.khai.gorbatiuk.taskmanager.exception.WrongUserDataException;
import ua.khai.gorbatiuk.taskmanager.service.CategoryService;
import ua.khai.gorbatiuk.taskmanager.service.TaskService;
import ua.khai.gorbatiuk.taskmanager.util.constant.Attributes;
import ua.khai.gorbatiuk.taskmanager.util.constant.RequestParameter;
import ua.khai.gorbatiuk.taskmanager.util.converter.Converter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Comparator;
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

        if (tasksBean.getTask().getId() != ROOT_TASK_ID) {
            tryToPutAllTasks(request, tasksBean);
        } else {
            try {
                putNeighbors(request, tasksBean.getUser().getId(), tasksBean.getTask().getId());
            } catch (WrongUserDataException e) {
                logger.debug(e.getMessage());
            }
        }
    }

    private void tryToPutAllTasks(HttpServletRequest request, TasksBean tasksBean) {
        try {
            Task currentTask = putCurrentTask(request, tasksBean);
            tasksBean.setTask(currentTask);
            putNeighbors(request, tasksBean.getUser().getId(), currentTask.getRootId());
            putChildren(request, tasksBean);
        } catch (WrongUserDataException e) {
            throw new CriticalUserDataException(e.getMessage());
        } catch (ServiceException e) {
            logger.debug(e);
        }
    }

    private void putNeighbors(HttpServletRequest request, Integer userId, Integer rootId) {
        List<Task> neighbors = taskService.getUncheckedByUserIdAndRootTaskId(userId, rootId);
        neighbors.sort(Comparator.comparing(Task::getId));
        request.setAttribute(NEIGHBORS, neighbors);
    }

    private Task putCurrentTask(HttpServletRequest request, TasksBean tasksBean) {
        Task currentTask = taskService.getByUserIdAndTaskId(tasksBean.getUser().getId(), tasksBean.getTask().getId());
        request.getSession().setAttribute(RequestParameter.CURRENT_TASK, currentTask);
        putCategories(request, tasksBean);
        return currentTask;
    }

    private void putCategories(HttpServletRequest request, TasksBean taskBean) {
        List<Category> categories = categoryService.getAllByUserId(taskBean.getUser().getId());
        request.setAttribute(ALL_CATEGORIES, categories);
    }

    private void putChildren(HttpServletRequest request, TasksBean tasksBean) {
        try {
            List<Task> children = taskService.getAllByTasksBean(tasksBean);
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
