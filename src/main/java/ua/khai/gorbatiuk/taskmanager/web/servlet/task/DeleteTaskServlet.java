package ua.khai.gorbatiuk.taskmanager.web.servlet.task;

import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import ua.khai.gorbatiuk.taskmanager.entity.bean.TasksBean;
import ua.khai.gorbatiuk.taskmanager.exception.WrongUserDataException;
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

public class DeleteTaskServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(EditTaskServlet.class);

    private static final String TASKS_URI = "/task";

    private TaskService taskService;
    private Converter<HttpServletRequest, TasksBean> requestToTasksBeanConverter;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TasksBean tasksBean = requestToTasksBeanConverter.convert(request);
        try{
            taskService.delete(tasksBean.getTask().getId(), tasksBean.getUser().getId());
            request.getSession().setAttribute(RequestParameter.CURRENT_TASK,
                    taskService.getByUserIdAndTaskId(tasksBean.getUser().getId(), tasksBean.getTask().getRootId()));

        } catch (WrongUserDataException e) {
            logger.debug(e.getMessage());
        }

        response.sendRedirect(TASKS_URI);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        taskService = (TaskService) getServletContext().getAttribute(Attributes.TASK_SERVICE);
        requestToTasksBeanConverter = (Converter<HttpServletRequest, TasksBean>)
                getServletContext().getAttribute(Attributes.REQUEST_TO_TASKS_BEAN_CONVERTER);
    }
}
