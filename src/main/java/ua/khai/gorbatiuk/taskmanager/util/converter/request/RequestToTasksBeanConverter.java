package ua.khai.gorbatiuk.taskmanager.util.converter.request;

import ua.khai.gorbatiuk.taskmanager.entity.bean.TasksBean;
import ua.khai.gorbatiuk.taskmanager.entity.model.User;
import ua.khai.gorbatiuk.taskmanager.exception.ConverterException;
import ua.khai.gorbatiuk.taskmanager.util.constant.RequestParameter;
import ua.khai.gorbatiuk.taskmanager.util.converter.Converter;

import javax.servlet.http.HttpServletRequest;

public class RequestToTasksBeanConverter implements Converter<HttpServletRequest, TasksBean> {

    private static final String ROOT_ID = "rootId";
    private static final String TASKS_ID = "tasksId";
    private static final String SELECTED_TASK_ID = "selectedTaskId";
    private static final String CURRENT_TASK_ID = "currentTaskId";


    @Override
    public TasksBean convert(HttpServletRequest source) throws ConverterException {
        User user = (User) source.getSession().getAttribute(RequestParameter.CURRENT_USER);

        TasksBean tasksBean = new TasksBean();
        tasksBean.setUserId(user.getId());
        setTasksId(source, tasksBean);
        setRootId(source, tasksBean);
        setSelectTaskId(source, tasksBean);

        setCurrentTaskId(source, tasksBean);

        return tasksBean;
    }

    private void setRootId(HttpServletRequest source, TasksBean tasksBean) {
        try{
            String rootId = source.getParameter(ROOT_ID);
            tasksBean.setRootId(Integer.parseInt(rootId));
        } catch (NumberFormatException e) {
            tasksBean.setRootId(1);
        }
    }

    private void setTasksId(HttpServletRequest source, TasksBean tasksBean) {
        try{
            String rootPanelId = source.getParameter(TASKS_ID);
            tasksBean.setTasksId(Integer.parseInt(rootPanelId));
        } catch (NumberFormatException e) {
            tasksBean.setTasksId(null);
        }
    }


    private void setSelectTaskId(HttpServletRequest source, TasksBean tasksBean) {
        try{
            String selectTaskId = source.getParameter(SELECTED_TASK_ID);
            tasksBean.setSelectedTaskId(Integer.parseInt(selectTaskId));
        } catch (NumberFormatException e) {
            tasksBean.setSelectedTaskId(null);
        }
    }

    private void setCurrentTaskId(HttpServletRequest source, TasksBean tasksBean) {
        try{
            String currentTaskId = source.getParameter(CURRENT_TASK_ID);
            tasksBean.setCurrentTaskId(Integer.parseInt(currentTaskId));
        } catch (NumberFormatException e) {
            tasksBean.setCurrentTaskId(null);
        }
    }

}
