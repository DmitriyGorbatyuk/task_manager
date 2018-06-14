package ua.khai.gorbatiuk.taskmanager.entity.bean;

import ua.khai.gorbatiuk.taskmanager.entity.model.Task;
import ua.khai.gorbatiuk.taskmanager.entity.model.User;

public class TasksBean {
    private User user;
    private Task task;
    private String sortField;
    private Boolean isAscending;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public Boolean getAscending() {
        return isAscending;
    }

    public void setAscending(Boolean ascending) {
        isAscending = ascending;
    }
}
