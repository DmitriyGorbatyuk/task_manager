package ua.khai.gorbatiuk.taskmanager.entity.bean;

import ua.khai.gorbatiuk.taskmanager.entity.model.Category;

public class CategoryTimeBean {
    private Category category;
    private Integer time;

    public CategoryTimeBean() {
    }

    public CategoryTimeBean(Category category, Integer time) {
        this.category = category;
        this.time = time;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public void addTime(Integer time) {
        this.time += time;
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
