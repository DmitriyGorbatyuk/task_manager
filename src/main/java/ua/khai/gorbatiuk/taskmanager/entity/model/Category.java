package ua.khai.gorbatiuk.taskmanager.entity.model;

public class Category extends DBEntity {
    private Integer rootId;
    private String name;
    private String color;

    public Category() {
    }

    public Category(Integer id, Integer rootId, String name, String color) {
        super(id);
        this.rootId = rootId;
        this.name = name;
        this.color = color;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
