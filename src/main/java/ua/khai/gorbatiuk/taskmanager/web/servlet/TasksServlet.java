package ua.khai.gorbatiuk.taskmanager.web.servlet;

import org.apache.log4j.Logger;
import ua.khai.gorbatiuk.taskmanager.entity.bean.TasksBean;
import ua.khai.gorbatiuk.taskmanager.entity.model.Category;
import ua.khai.gorbatiuk.taskmanager.exception.WrongUserDataException;
import ua.khai.gorbatiuk.taskmanager.service.CategoryService;
import ua.khai.gorbatiuk.taskmanager.util.constant.Attributes;
import ua.khai.gorbatiuk.taskmanager.entity.model.Task;
import ua.khai.gorbatiuk.taskmanager.service.TaskService;
import ua.khai.gorbatiuk.taskmanager.util.converter.Converter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class TasksServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(LoginServlet.class);

    private static final String TASKS_PAGE = "jsp/tasks.jsp";
    private static final String TASKS_URI = "/tasks";
    private static final String NEIGHBORS = "neighbors";
    private static final String CHILDREN = "children";
    private static final String CURRENT_TASK = "currentTask";
    private static final String ALL_CATEGORIES = "allCategories";

    private TaskService taskService;
    private CategoryService categoryService;
    private Converter<HttpServletRequest, TasksBean> requestToTasksBeanConverter;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        putTasks(request);

        request.getRequestDispatcher(TASKS_PAGE).forward(request, response);
    }

    private void putTasks(HttpServletRequest request) {
        TasksBean tasksBean = requestToTasksBeanConverter.convert(request);
        List<Task> neighbors = null;
        if (tasksBean.getCurrentTaskId() != null) {
            try {
                Task currentTask = taskService.getByUserIdAndTaskId(tasksBean.getUserId(), tasksBean.getCurrentTaskId());
                request.setAttribute(CURRENT_TASK, currentTask);
                putCategories(request);
                neighbors = taskService.getAllByUserIdAndRootTaskId(tasksBean.getUserId(), currentTask.getRootId());
                List<Task> tasks = taskService.getAllByUserIdAndRootTaskId(tasksBean.getUserId(), tasksBean.getCurrentTaskId());
                request.setAttribute(CHILDREN, tasks);
            } catch (WrongUserDataException e) {
                //then do not put any tasks in request
            }
        } else {
            neighbors = taskService.getAllByUserIdAndRootTaskId(tasksBean.getUserId(), 1);
        }
        request.setAttribute(NEIGHBORS, neighbors);
    }

    private void putCategories(HttpServletRequest request) {
        List<Category> categories = categoryService.getAll();
        request.setAttribute(ALL_CATEGORIES, categories);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
