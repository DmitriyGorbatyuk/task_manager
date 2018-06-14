package ua.khai.gorbatiuk.taskmanager.service;

import ua.khai.gorbatiuk.taskmanager.entity.model.Category;

import java.util.List;


public interface CategoryService {
    List<Category> getAllByUserId(Integer userId);
    Category getByUserIdAndCategoryId(Integer userId, Integer categoryId);
    List<Category> getAllByUserIdAndCategoryRootId(Integer userId, Integer id);

    void add(Integer id, Integer rootId, String categoryName);

    void delete(Integer id, Integer rootId);

    void update(Category category);
}
