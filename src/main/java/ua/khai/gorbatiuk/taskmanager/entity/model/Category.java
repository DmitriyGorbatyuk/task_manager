package ua.khai.gorbatiuk.taskmanager.entity.model;

public class Category extends DBEntity {
    private Integer idRoot;
    private String name;
    private String color;

    public Category() {
    }

    public Category(Integer id, Integer idRoot, String name, String color) {
        super(id);
        this.idRoot = idRoot;
        this.name = name;
        this.color = color;
    }

    public Integer getIdRoot() {
        return idRoot;
    }

    public void setIdRoot(Integer idRoot) {
        this.idRoot = idRoot;
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
