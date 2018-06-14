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
    public List<Category> getAllByUserId(Integer userId) {
        try {
            return transactionManager.transact(() -> categoryDao.getAllByUserId(userId));
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Category getByUserIdAndCategoryId(Integer userId, Integer id) {
        try {
            return transactionManager.transact(() ->  {
                Category category = categoryDao.getByUserIdAndCategoryId(userId, id);
                if(category == null) {
                    throw new WrongUserDataException("There is not category with id:" + id);
                }
                return category;
            });
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Category> getAllByUserIdAndCategoryRootId(Integer userId, Integer id) {
        try {
            return transactionManager.transact(() ->  {
                List<Category> categories = categoryDao.getALlByUserIdAndCategoryRootId(userId, id);
                if(categories.isEmpty()) {
                    throw new WrongUserDataException("There is not category with root id:" + id);
                }
                return categories;
            });
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void add(Integer id, Integer rootId, String categoryName) {
        try {
            transactionManager.transact(() ->  {
                categoryDao.add(id, rootId, categoryName);
                return null;
            });
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(Integer id, Integer rootId) {
        try {
            transactionManager.transact(() ->  {
                categoryDao.delete(id, rootId);
                return null;
            });
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(Category category) {
        try {
            transactionManager.transact(() ->  {
                categoryDao.update(category);
                return null;
            });
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
