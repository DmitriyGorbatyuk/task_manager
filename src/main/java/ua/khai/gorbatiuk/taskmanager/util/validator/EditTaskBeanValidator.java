package ua.khai.gorbatiuk.taskmanager.util.validator;

import ua.khai.gorbatiuk.taskmanager.entity.bean.EditTaskBean;

import java.util.HashMap;
import java.util.Map;

public class EditTaskBeanValidator {

    private static final int MAX_TASK_NAME_LENGTH = 44;
    private static final int MAX_TASK_DESCRIPTION_LENGTH = 16_777_215;

    public Map<String, String> validate(EditTaskBean task) {
        Map<String, String> map = new HashMap<>();

        validateTaskName(task, map);
        validateTaskComplexity(task, map);
        validateTaskDescription(task, map);
        validateTaskTime(task, map);
        validateTaskCategory(task, map);

        return map;
    }

    private void validateTaskName(EditTaskBean task, Map<String, String> map) {
        if(isEmpty(task.getName())) {
            map.put("taskName", "This field cannot be empty");
        }
        if(task.getName().length() > MAX_TASK_NAME_LENGTH){
            map.put("taskName", "This field cannot be bigger than 44 symbols");
        }
    }

    private void validateTaskComplexity(EditTaskBean task, Map<String, String> map) {
        if(task.getComplexity() == null) {
            map.put("taskComplexity", "This field cannot be empty");
        }
        if(task.getComplexity() > 10 || task.getComplexity() < 0){
            map.put("taskComplexity", "This field have to be between 0 and 10");
        }
    }

    private void validateTaskDescription(EditTaskBean task, Map<String, String> map) {
        if(task.getDescription().getBytes().length > MAX_TASK_DESCRIPTION_LENGTH) {
            map.put("taskDescription", "This field is too big");
        }
    }

    private void validateTaskTime(EditTaskBean task, Map<String, String> map) {
        if(task.getComplexity() == null) {
            map.put("taskTime", "This field cannot be empty");
        }
        if(task.getTime() <0){
            map.put("taskTime", "This field have to be more or equal 0 ");
        }
    }

    private void validateTaskCategory(EditTaskBean task, Map<String, String> map) {
        if(task.getFkCategory() == null) {
            map.put("taskCategory", "This field cannot be empty");
        }
        if(task.getFkCategory() <0){
            map.put("taskTime", "This field have to be more or equal 0 ");
        }
    }


    private boolean isEmpty(String name) {
        return name == null || name.equals("");
    }
}
