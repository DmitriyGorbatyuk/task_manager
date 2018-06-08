package ua.khai.gorbatiuk.taskmanager.entity.bean;

public class AddTaskBean {
    String newTaskName;
    String position;

    public String getNewTaskName() {
        return newTaskName;
    }

    public void setNewTaskName(String newTaskName) {
        this.newTaskName = newTaskName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
