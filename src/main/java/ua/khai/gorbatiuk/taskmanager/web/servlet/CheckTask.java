package ua.khai.gorbatiuk.taskmanager.web.servlet;

import org.apache.log4j.Logger;
import ua.khai.gorbatiuk.taskmanager.entity.bean.ExecutingTaskBean;
import ua.khai.gorbatiuk.taskmanager.entity.bean.TasksBean;
import ua.khai.gorbatiuk.taskmanager.entity.model.Task;
import ua.khai.gorbatiuk.taskmanager.exception.ConverterException;
import ua.khai.gorbatiuk.taskmanager.exception.CriticalUserDataException;
import ua.khai.gorbatiuk.taskmanager.exception.WrongUserDataException;
import ua.khai.gorbatiuk.taskmanager.service.TaskService;
import ua.khai.gorbatiuk.taskmanager.util.constant.Attributes;
import ua.khai.gorbatiuk.taskmanager.util.converter.Converter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class CheckTask extends HttpServlet {

    private static final Logger logger = Logger.getLogger(ExecuteTaskServlet.class);

    private static final String TASKS_URI = "/tasks";

    private TaskService taskService;
    private Converter<HttpServletRequest, ExecutingTaskBean> requestToExecutingTaskBean;
    private Converter<HttpServletRequest, TasksBean> requestToTasksBeanConverter;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            checkTask(request);
            response.sendRedirect(TASKS_URI);
        } catch (ConverterException e) {
            logger.debug(e);
            throw new CriticalUserDataException(e.getMessage());
        }
    }

    private void checkTask(HttpServletRequest request) {
        ExecutingTaskBean executingTaskBean = requestToExecutingTaskBean.convert(request);
        TasksBean tasksBean = requestToTasksBeanConverter.convert(request);
        try {
            Task executingTask = taskService.getByUserIdAndTaskId(tasksBean.getUserId(), executingTaskBean.getExecutingTaskId());
            HttpSession session = request.getSession();
            if (executingTask.getIsLeaf()) {
                taskService.checkTask(executingTask);
            } else {
                session.setAttribute("executeTaskError", "You can only check subtasks");
            }
        } catch (WrongUserDataException e) {
            throw new CriticalUserDataException(e.getMessage());
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        taskService = (TaskService) getServletContext().getAttribute(Attributes.TASK_SERVICE);
        requestToTasksBeanConverter = (Converter<HttpServletRequest, TasksBean>)
                getServletContext().getAttribute(Attributes.REQUEST_TO_TASKS_BEAN_CONVERTER);
        requestToExecutingTaskBean = (Converter<HttpServletRequest, ExecutingTaskBean>)
                getServletContext().getAttribute(Attributes.REQUEST_TO_EXECUTING_TASK_BEAN_CONVERTER);
    }
}
