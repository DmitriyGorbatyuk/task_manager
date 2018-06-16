package ua.khai.gorbatiuk.taskmanager.entity.bean;

import java.time.LocalDateTime;

public class EditTaskBean {
    private String name;
    private LocalDateTime date;
    private Integer repeatAfter;
    private Integer complexity;
    private String description;
    private Integer time;
    private Integer fkCategory;

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

    public Integer getRepeatAfter() {
        return repeatAfter;
    }

    public void setRepeatAfter(Integer repeatAfter) {
        this.repeatAfter = repeatAfter;
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

    public Integer getFkCategory() {
        return fkCategory;
    }

    public void setFkCategory(Integer fkCategory) {
        this.fkCategory = fkCategory;
    }
}
