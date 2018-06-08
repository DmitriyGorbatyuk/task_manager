package ua.khai.gorbatiuk.taskmanager.util.converter.request;

import ua.khai.gorbatiuk.taskmanager.entity.bean.ExecutingTaskBean;
import ua.khai.gorbatiuk.taskmanager.exception.ConverterException;
import ua.khai.gorbatiuk.taskmanager.util.converter.Converter;

import javax.servlet.http.HttpServletRequest;

public class RequestToExecutingTaskBeanConverter implements Converter<HttpServletRequest, ExecutingTaskBean> {
    @Override
    public ExecutingTaskBean convert(HttpServletRequest source) throws ConverterException {
        ExecutingTaskBean executingTaskBean = new ExecutingTaskBean();

        try{
            String id = source.getParameter("executingTaskBeanId");
            executingTaskBean.setExecutingTaskId(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            throw new ConverterException("Cannot convert Request to ExecutingTaskBean", e);
        }

        return executingTaskBean;
    }
}
