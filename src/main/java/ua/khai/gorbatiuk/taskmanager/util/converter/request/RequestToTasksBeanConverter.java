package ua.khai.gorbatiuk.taskmanager.util.converter.request;

import ua.khai.gorbatiuk.taskmanager.entity.bean.TasksBean;
import ua.khai.gorbatiuk.taskmanager.entity.model.Task;
import ua.khai.gorbatiuk.taskmanager.entity.model.User;
import ua.khai.gorbatiuk.taskmanager.exception.ConverterException;
import ua.khai.gorbatiuk.taskmanager.util.constant.RequestParameter;
import ua.khai.gorbatiuk.taskmanager.util.converter.Converter;

import javax.servlet.http.HttpServletRequest;

import static ua.khai.gorbatiuk.taskmanager.util.constant.RequestParameter.CURRENT_TASK;

public class RequestToTasksBeanConverter implements Converter<HttpServletRequest, TasksBean> {

    private static final String CURRENT_TASK_ID = "currentTaskId";

    @Override
    public TasksBean convert(HttpServletRequest source) throws ConverterException {
        User user = (User) source.getSession().getAttribute(RequestParameter.CURRENT_USER);

        TasksBean tasksBean = new TasksBean();
        tasksBean.setUserId(user.getId());
        setCurrentTaskId(source, tasksBean);

        return tasksBean;
    }

    private void setCurrentTaskId(HttpServletRequest source, TasksBean tasksBean) {
        try {
            String currentTaskId = source.getParameter(CURRENT_TASK_ID);
            if (currentTaskId != null) {
                tasksBean.setCurrentTaskId(Integer.parseInt(currentTaskId));
            } else {
                Task currentTask = (Task) source.getSession().getAttribute(CURRENT_TASK);
                tasksBean.setCurrentTaskId(currentTask.getId());
            }
        } catch (NumberFormatException | NullPointerException e) {
            tasksBean.setCurrentTaskId(1);
        }
    }

}
