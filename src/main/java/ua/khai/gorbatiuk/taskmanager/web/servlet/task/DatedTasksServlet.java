package ua.khai.gorbatiuk.taskmanager.web.servlet.task;

import ua.khai.gorbatiuk.taskmanager.entity.bean.TasksBean;
import ua.khai.gorbatiuk.taskmanager.entity.model.Task;
import ua.khai.gorbatiuk.taskmanager.service.TaskService;
import ua.khai.gorbatiuk.taskmanager.util.constant.Attributes;
import ua.khai.gorbatiuk.taskmanager.util.constant.RequestParameter;
import ua.khai.gorbatiuk.taskmanager.util.converter.Converter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class DatedTasksServlet extends HttpServlet {
    private static final String TODAY_PAGE = "jsp/datedTasks.jsp";
    private static final String TODAY_URI = "todaysTasks";

    private TaskService taskService;
    private Converter<HttpServletRequest, TasksBean> requestToTasksBeanConverter;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        putTasks(request);

        request.getRequestDispatcher(TODAY_PAGE).forward(request, response);
    }

    private void putTasks(HttpServletRequest request) {
        TasksBean tasksBean = requestToTasksBeanConverter.convert(request);
        List<Task> tasks = taskService.getDatedTasks(tasksBean);
        request.setAttribute(RequestParameter.DATED_TASKS, tasks);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        taskService = (TaskService) getServletContext().getAttribute(Attributes.TASK_SERVICE);
        requestToTasksBeanConverter = (Converter<HttpServletRequest, TasksBean>)
                getServletContext().getAttribute(Attributes.REQUEST_TO_TASKS_BEAN_CONVERTER);
    }
}
