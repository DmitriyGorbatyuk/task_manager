package ua.khai.gorbatiuk.taskmanager.web.servlet;

import org.apache.log4j.Logger;
import ua.khai.gorbatiuk.taskmanager.entity.bean.AddTaskBean;
import ua.khai.gorbatiuk.taskmanager.entity.bean.TasksBean;
import ua.khai.gorbatiuk.taskmanager.entity.model.Task;
import ua.khai.gorbatiuk.taskmanager.entity.model.User;
import ua.khai.gorbatiuk.taskmanager.exception.ConverterException;
import ua.khai.gorbatiuk.taskmanager.exception.CriticalUserDataException;
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
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class AddTaskServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(AddTaskServlet.class);
    private static final String TASKS_URI = "/tasks";

    private TaskService taskService;
    private Converter<HttpServletRequest, TasksBean> requestToTasksBeanConverter;
    private Converter<HttpServletRequest, AddTaskBean> requestToAddTaskBeanConverter;
    private Map<String, Supplier<Task>> map;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            addNewTask(request);
            response.sendRedirect(TASKS_URI);
        } catch (ConverterException e) {
            logger.debug(e);
            throw new CriticalUserDataException(e.getMessage());
        }
    }

    private void addNewTask(HttpServletRequest request) {
        TasksBean tasksBean = requestToTasksBeanConverter.convert(request);
        AddTaskBean addTaskBean = requestToAddTaskBeanConverter.convert(request);

        Task newTask = fillNewTask(tasksBean, addTaskBean);

        taskService.add(newTask);
    }

    private Task fillNewTask(TasksBean tasksBean, AddTaskBean addTaskBean) {
        map.put(RequestParameter.TASK_ADD_POSITION_NEIGHBORS,() -> {
            Task newTask = new Task();
            newTask.setRootId(tasksBean.getCurrentTaskId());
            return newTask;
        });
        map.put(RequestParameter.TASK_ADD_POSITION_CHILDREN, () -> {
            Task currentTask = taskService.getByUserIdAndTaskId(tasksBean.getUserId(), tasksBean.getCurrentTaskId());
            Task newTask = new Task();
            newTask.setRootId(currentTask.getId());
            return newTask;
        });
        Task newTask = map.get(addTaskBean.getPosition()).get();
        newTask.setName(addTaskBean.getNewTaskName());
        User user = new User();
        user.setId(tasksBean.getUserId());
        newTask.setUser(user);
        return newTask;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        taskService = (TaskService) getServletContext().getAttribute(Attributes.TASK_SERVICE);
        requestToTasksBeanConverter = (Converter<HttpServletRequest, TasksBean>)
                getServletContext().getAttribute(Attributes.REQUEST_TO_TASKS_BEAN_CONVERTER);
        requestToAddTaskBeanConverter = (Converter<HttpServletRequest, AddTaskBean>)
                getServletContext().getAttribute(Attributes.REQUEST_TO_ADD_TASK_BEAN_CONVERTER);
        map = new HashMap<>();
    }
}
