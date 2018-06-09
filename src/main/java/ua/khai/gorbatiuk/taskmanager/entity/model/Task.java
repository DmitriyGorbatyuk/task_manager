package ua.khai.gorbatiuk.taskmanager.entity.model;

import java.time.LocalDateTime;

public class Task extends DBEntity {
    private Integer rootId;
    private String name;
    private LocalDateTime date;
    private Integer complexity;
    private String description;
    private Integer time;
    private Boolean checked;
    private Category category;
    private User user;
    private Boolean isLeaf;

    public Task() {
    }

    public Task(Integer id, Integer rootId, String name, LocalDateTime date, Integer complexity, String description,
                Integer time, Boolean checked, Category category, User user) {
        super(id);
        this.rootId = rootId;
        this.name = name;
        this.date = date;
        this.complexity = complexity;
        this.description = description;
        this.time = time;
        this.checked = checked;
        this.category = category;
        this.user = user;
    }

    public Integer getRootId() {
        return rootId;
    }

    public void setRootId(Integer rootId) {
        this.rootId = rootId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Integer getComplexity() {
        return complexity;
    }

    public void setComplexity(Integer complexity) {
        this.complexity = complexity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(Boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public String getFormattedTime() {
        StringBuilder formattedTime = new StringBuilder();

        int hours = time / (60 * 60);
        if (hours < 10) {
            formattedTime.append('0');
        }
        formattedTime.append(hours);
        formattedTime.append(':');

        int minutes = (time / (60)) % 60;
        if (minutes < 10) {
            formattedTime.append('0');
        }
        formattedTime.append(minutes);
        formattedTime.append(':');

        int seconds = time % 60;
        if (seconds < 10) {
            formattedTime.append('0');
        }
        formattedTime.append(seconds);
        return formattedTime.toString();
    }
}
