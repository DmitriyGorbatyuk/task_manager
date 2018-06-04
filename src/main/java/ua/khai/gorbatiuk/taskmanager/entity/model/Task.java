package ua.khai.gorbatiuk.taskmanager.entity.model;

import java.sql.Date;

public class Task extends DBEntity {
    private Integer rootId;
    private String name;
    private Date date;
    private Integer complexity;
    private String description;
    private Integer time;
    private Boolean checked;
    private Category category;
    private User user;

    public Task() {
    }

    public Task(Integer id, Integer rootId, String name, Date date, Integer complexity, String description, Integer time, Category category, User user) {
        super(id);
        this.rootId = rootId;
        this.name = name;
        this.date = date;
        this.complexity = complexity;
        this.description = description;
        this.time = time;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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
}