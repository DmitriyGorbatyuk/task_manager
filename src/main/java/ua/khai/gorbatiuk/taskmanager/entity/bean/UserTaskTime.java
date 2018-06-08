package ua.khai.gorbatiuk.taskmanager.entity.bean;

import java.time.LocalDateTime;

public class UserTaskTime {
    private Integer userId;
    private Integer taskId;
    private LocalDateTime time;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
