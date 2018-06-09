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
    public List<Category> getAllByUserId() {
        try {
            return transactionManager.transact(() -> categoryDao.getAll());
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Category getByUserIdAndCategoryId(Integer id) {
        try {
            return transactionManager.transact(() ->  {
                Category category = categoryDao.getById(id);
                if(category == null) {
                    throw new WrongUserDataException("There is not category with id:" + id);
                }
                return category;
            });
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
