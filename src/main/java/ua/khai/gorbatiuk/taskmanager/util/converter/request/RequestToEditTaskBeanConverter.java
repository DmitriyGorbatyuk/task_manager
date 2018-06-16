package ua.khai.gorbatiuk.taskmanager.util.converter.request;

import ua.khai.gorbatiuk.taskmanager.entity.bean.EditTaskBean;
import ua.khai.gorbatiuk.taskmanager.exception.ConverterException;
import ua.khai.gorbatiuk.taskmanager.util.converter.Converter;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

public class RequestToEditTaskBeanConverter implements Converter<HttpServletRequest, EditTaskBean> {
    @Override
    public EditTaskBean convert(HttpServletRequest source) throws ConverterException {
        EditTaskBean bean = new EditTaskBean();

        bean.setName(source.getParameter("taskName"));
        setDate(source, bean);
        setRepeatAfter(source, bean);

        setComplexity(source, bean);
        bean.setDescription(source.getParameter("taskDescription"));
        setTime(source, bean);
        setCategoryId(source, bean);

        return bean;
    }

    private void setRepeatAfter(HttpServletRequest source, EditTaskBean bean) {
        try{
            String stringRepeatAfter = source.getParameter("repeatAfter");
            bean.setRepeatAfter(Integer.parseInt(stringRepeatAfter));
        } catch (NumberFormatException e) {
            bean.setRepeatAfter(null);
        }
    }

    private void setDate(HttpServletRequest source, EditTaskBean bean) {
        String date = source.getParameter("taskDate");
        if(date == null || date.equals("")) {
            bean.setDate(null);
        } else {
            bean.setDate(LocalDateTime.parse(date));
        }
    }


    private void setComplexity(HttpServletRequest source, EditTaskBean bean) {
        try{
            String stringComplexity = source.getParameter("taskComplexity");
            bean.setComplexity(Integer.parseInt(stringComplexity));
        } catch (NumberFormatException e) {
            bean.setComplexity(0);
        }
    }

    private void setTime(HttpServletRequest source, EditTaskBean bean) {
        try{
            String stringTime = source.getParameter("taskTime");
            bean.setTime(Integer.parseInt(stringTime));
        } catch (NumberFormatException e) {
            bean.setTime(0);
        }
    }

    private void setCategoryId(HttpServletRequest source, EditTaskBean bean) {
        try{
            String stringCategoryId = source.getParameter("taskCategoryId");
            bean.setFkCategory(Integer.parseInt(stringCategoryId));
        } catch (NumberFormatException e) {
            bean.setFkCategory(1);
        }
    }
}
