package ua.khai.gorbatiuk.taskmanager.entity.model;

public abstract class DBEntity {
    private Integer id;

    public DBEntity() {
    }

    public DBEntity(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
