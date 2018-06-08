package ua.khai.gorbatiuk.taskmanager.util.converter;

import ua.khai.gorbatiuk.taskmanager.entity.bean.EditTaskBean;
import ua.khai.gorbatiuk.taskmanager.entity.model.Task;
import ua.khai.gorbatiuk.taskmanager.exception.ConverterException;

public class EditTaskBeanToTaskConverter implements Converter<EditTaskBean, Task> {
    @Override
    public Task convert(EditTaskBean source) throws ConverterException {
        Task task = new Task();

        task.setName(source.getName());
        task.setTime(source.getTime());
        task.setDescription(source.getDescription());
        task.setDate(source.getDate());
        task.setComplexity(source.getComplexity());

        return task;
    }
}
