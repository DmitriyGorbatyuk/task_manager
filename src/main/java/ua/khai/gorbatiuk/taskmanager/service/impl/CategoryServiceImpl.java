package ua.khai.gorbatiuk.taskmanager.service.impl;

import ua.khai.gorbatiuk.taskmanager.dao.CategoryDao;
import ua.khai.gorbatiuk.taskmanager.dao.connection.MysqlTransactionManager;
import ua.khai.gorbatiuk.taskmanager.entity.model.Category;
import ua.khai.gorbatiuk.taskmanager.exception.DaoException;
import ua.khai.gorbatiuk.taskmanager.exception.ServiceException;
import ua.khai.gorbatiuk.taskmanager.exception.WrongUserDataException;
import ua.khai.gorbatiuk.taskmanager.service.CategoryService;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {

    private MysqlTransactionManager transactionManager;

    private CategoryDao categoryDao;

    public CategoryServiceImpl(MysqlTransactionManager transactionManager, CategoryDao categoryDao) {
        this.transactionManager = transactionManager;
        this.categoryDao = categoryDao;
    }

    @Override
    public List<Category> getAll() {
        try {
            return transactionManager.transact(() -> categoryDao.getAll());
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
