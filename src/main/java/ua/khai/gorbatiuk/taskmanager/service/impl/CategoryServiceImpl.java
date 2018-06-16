package ua.khai.gorbatiuk.taskmanager.service.impl;

import ua.khai.gorbatiuk.taskmanager.dao.CategoryDao;
import ua.khai.gorbatiuk.taskmanager.dao.TaskDao;
import ua.khai.gorbatiuk.taskmanager.dao.connection.MysqlTransactionManager;
import ua.khai.gorbatiuk.taskmanager.entity.model.Category;
import ua.khai.gorbatiuk.taskmanager.entity.model.Task;
import ua.khai.gorbatiuk.taskmanager.exception.DaoException;
import ua.khai.gorbatiuk.taskmanager.exception.ServiceException;
import ua.khai.gorbatiuk.taskmanager.exception.WrongUserDataException;
import ua.khai.gorbatiuk.taskmanager.service.CategoryService;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {

    private MysqlTransactionManager transactionManager;

    private CategoryDao categoryDao;
    private TaskDao taskDao;

    public CategoryServiceImpl(MysqlTransactionManager transactionManager, CategoryDao categoryDao, TaskDao taskDao) {
        this.transactionManager = transactionManager;
        this.categoryDao = categoryDao;
        this.taskDao = taskDao;
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
    public void add(Category newCategory) {
        try {
            transactionManager.transact(() ->  {
                Category rootCategory = categoryDao.getByCategoryId(newCategory.getRootId());
                newCategory.setColor(rootCategory.getColor());
                categoryDao.add(newCategory);
                return null;
            });
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(Integer id, Integer userId) {
        try {
            transactionManager.transact(() ->  {
                List<Category> children = categoryDao.getALlByUserIdAndCategoryRootId(userId, id);
                List<Task> tasks = taskDao.getAllByCategoryId(id);
                if(children.isEmpty() && tasks.isEmpty()) {
                    categoryDao.delete(id, userId);
                } else {
                 throw new WrongUserDataException("Category cannot be deleted, it has subcategories or related tasks");
                }
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
