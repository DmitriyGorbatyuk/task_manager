package ua.khai.gorbatiuk.taskmanager.util.converter.request;

import ua.khai.gorbatiuk.taskmanager.entity.bean.AddTaskBean;
import ua.khai.gorbatiuk.taskmanager.exception.ConverterException;
import ua.khai.gorbatiuk.taskmanager.util.constant.RequestParameter;
import ua.khai.gorbatiuk.taskmanager.util.converter.Converter;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RequestToAddBeanConverter implements Converter<HttpServletRequest, AddTaskBean> {
    private static final Set< String> positions = new HashSet<>(Arrays.asList(
            RequestParameter.TASK_ADD_POSITION_NEIGHBORS,
            RequestParameter.TASK_ADD_POSITION_CHILDREN));
    private static final int MAX_TASK_NAME_LENGTH = 44;

    @Override
    public AddTaskBean convert(HttpServletRequest source) throws ConverterException {
        AddTaskBean addTaskBean = new AddTaskBean();

        putNewTaskName(source, addTaskBean);
        putPosition(source, addTaskBean);

        return addTaskBean;
    }

    private void putNewTaskName(HttpServletRequest source, AddTaskBean addTaskBean) {
        String newTaskName = source.getParameter("newTaskName");
        if(newTaskName == null || newTaskName.equals("") || newTaskName.length() > MAX_TASK_NAME_LENGTH) {
            throw new ConverterException("Cannot convert Request to AddTaskBean");
        }
        addTaskBean.setNewTaskName(newTaskName);
    }

    private void putPosition(HttpServletRequest source, AddTaskBean addTaskBean) {
        String position = source.getParameter(RequestParameter.TASK_ADD_POSITION);
        if(!positions.contains(position)) {
            throw new ConverterException("Cannot convert Request to AddTaskBean");
        }
        addTaskBean.setPosition(position);
    }
}
