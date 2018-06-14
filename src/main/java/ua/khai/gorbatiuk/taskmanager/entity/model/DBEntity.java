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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DBEntity dbEntity = (DBEntity) o;

        return id != null ? id.equals(dbEntity.id) : dbEntity.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
