package ua.khai.gorbatiuk.taskmanager.entity.bean;

public class TasksBean {
    private Integer rootId;
    private Integer tasksId;
    private Integer selectedTaskId;
    private Integer userId;
    private Integer currentTaskId;

    public Integer getRootId() {
        return rootId;
    }

    public void setRootId(Integer rootId) {
        this.rootId = rootId;
    }

    public Integer getTasksId() {
        return tasksId;
    }

    public void setTasksId(Integer tasksId) {
        this.tasksId = tasksId;
    }

    public Integer getSelectedTaskId() {
        return selectedTaskId;
    }

    public void setSelectedTaskId(Integer selectedTaskId) {
        this.selectedTaskId = selectedTaskId;
    }

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
