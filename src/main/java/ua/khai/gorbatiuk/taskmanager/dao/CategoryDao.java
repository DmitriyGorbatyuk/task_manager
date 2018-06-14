package ua.khai.gorbatiuk.taskmanager.dao;

import ua.khai.gorbatiuk.taskmanager.entity.model.Category;

import java.util.List;

public interface CategoryDao {
    List<Category> getAllByUserId(Integer userId);
    Category getByUserIdAndCategoryId(Integer userId, Integer categoryId);
    List<Category> getALlByUserIdAndCategoryRootId(Integer userId, Integer categoryId);

    void add(Integer id, Integer rootId, String categoryName);

    void delete(Integer id, Integer rootId);

    void update(Category category);
}
