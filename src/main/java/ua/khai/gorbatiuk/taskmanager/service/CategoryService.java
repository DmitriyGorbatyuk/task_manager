package ua.khai.gorbatiuk.taskmanager.service;

import ua.khai.gorbatiuk.taskmanager.entity.model.Category;

import java.util.List;

//TODO correct category service implementation
public interface CategoryService {
    List<Category> getAllByUserId(Integer userId);
    Category getByUserIdAndCategoryId(Integer id);
}
