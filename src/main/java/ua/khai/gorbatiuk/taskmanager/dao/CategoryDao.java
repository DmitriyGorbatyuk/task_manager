package ua.khai.gorbatiuk.taskmanager.dao;

import ua.khai.gorbatiuk.taskmanager.entity.model.Category;

import java.util.List;

public interface CategoryDao {
    List<Category> getAll();
}
