package ua.khai.gorbatiuk.taskmanager.entity.bean;

public class TasksBean {
    private Integer userId;
    private Integer currentTaskId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCurrentTaskId() {
        return currentTaskId;
    }

    public void setCurrentTaskId(Integer currentTaskId) {
        this.currentTaskId = currentTaskId;
    }
}
