package ua.khai.gorbatiuk.taskmanager.util.converter.request;

import ua.khai.gorbatiuk.taskmanager.entity.bean.TasksBean;
import ua.khai.gorbatiuk.taskmanager.entity.model.Task;
import ua.khai.gorbatiuk.taskmanager.entity.model.User;
import ua.khai.gorbatiuk.taskmanager.exception.ConverterException;
import ua.khai.gorbatiuk.taskmanager.util.constant.RequestParameter;
import ua.khai.gorbatiuk.taskmanager.util.converter.Converter;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static ua.khai.gorbatiuk.taskmanager.util.constant.RequestParameter.CURRENT_TASK;

public class RequestToTasksBeanConverter implements Converter<HttpServletRequest, TasksBean> {

    private static final String CURRENT_TASK_ID = "currentTaskId";

    private static final Set<String> sortFields = new HashSet<>(Arrays.asList("name", "complexity", "category", "time", "none", "date"));

    @Override
    public TasksBean convert(HttpServletRequest source) throws ConverterException {
        User user = (User) source.getSession().getAttribute(RequestParameter.CURRENT_USER);

        TasksBean tasksBean = new TasksBean();
        tasksBean.setUser(user);

        setCurrentTask(source, tasksBean);

        setSort(source, tasksBean);
        return tasksBean;
    }

    private void setCurrentTask(HttpServletRequest source, TasksBean tasksBean) {
        try {
            String currentTaskId = source.getParameter(CURRENT_TASK_ID);
            if (currentTaskId != null) {
                Task currentTask = new Task();
                currentTask.setId(Integer.parseInt(currentTaskId));
                tasksBean.setTask(currentTask);
            } else {
                Task currentTask = (Task) source.getSession().getAttribute(CURRENT_TASK);
                currentTask.getId();
                tasksBean.setTask(currentTask);
            }
        } catch (NumberFormatException | NullPointerException e) {
            Task currentTask = new Task();
            currentTask.setId(1);
            tasksBean.setTask(currentTask);
        }
    }

    private void setSort(HttpServletRequest source, TasksBean tasksBean) {
        tasksBean.setSortField((String) source.getSession().getAttribute("sort"));
        tasksBean.setAscending((Boolean) source.getSession().getAttribute("asc"));
        if (tasksBean.getAscending() == null) {
            tasksBean.setAscending(true);
        }
        String sortField = source.getParameter("sort");

        if (sortFields.contains(sortField)) {
            if (tasksBean.getSortField() != null && !sortField.equals("none") && tasksBean.getSortField().equals(sortField)) {
                tasksBean.setAscending(!tasksBean.getAscending());
            }
            tasksBean.setSortField(sortField);
            source.getSession().setAttribute("sort", sortField);
            source.getSession().setAttribute("asc", tasksBean.getAscending());
        } else {
            tasksBean.setSortField("none");
        }
    }
}
