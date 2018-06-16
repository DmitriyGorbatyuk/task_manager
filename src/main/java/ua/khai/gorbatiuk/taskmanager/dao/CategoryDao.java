package ua.khai.gorbatiuk.taskmanager.dao;

import ua.khai.gorbatiuk.taskmanager.entity.model.Category;

import java.util.List;

public interface CategoryDao {
    Category getByCategoryId(Integer categoryId);
    List<Category> getAllByUserId(Integer userId);
    Category getByUserIdAndCategoryId(Integer userId, Integer categoryId);
    List<Category> getALlByUserIdAndCategoryRootId(Integer userId, Integer categoryId);

    void delete(Integer id, Integer userId);

    void update(Category category);

    void add(Category newCategory);
}
