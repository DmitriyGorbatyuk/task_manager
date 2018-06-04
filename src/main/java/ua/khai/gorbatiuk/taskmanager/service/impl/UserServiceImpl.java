package ua.khai.gorbatiuk.taskmanager.service.impl;


import ua.khai.gorbatiuk.taskmanager.util.constant.Message;
import ua.khai.gorbatiuk.taskmanager.dao.UserDao;
import ua.khai.gorbatiuk.taskmanager.dao.connection.MysqlTransactionManager;
import ua.khai.gorbatiuk.taskmanager.exception.DaoException;
import ua.khai.gorbatiuk.taskmanager.exception.ServiceException;
import ua.khai.gorbatiuk.taskmanager.exception.WrongUserDataException;
import ua.khai.gorbatiuk.taskmanager.entity.model.User;
import ua.khai.gorbatiuk.taskmanager.service.UserService;

public class UserServiceImpl implements UserService {

    private MysqlTransactionManager transactionManager;

    private UserDao userDAO;

    public UserServiceImpl(UserDao userDAO, MysqlTransactionManager transactionManager) {
        this.userDAO = userDAO;
        this.transactionManager = transactionManager;
    }

    public User register(User user) {
        try {
            return transactionManager.transact(() -> {
                checkUserForExistence(user);
                return userDAO.add(user);
            });
        } catch (DaoException e) {
            throw new ServiceException(e);
        }

    }

    private void checkUserForExistence(User user) {
        if (contains(user)) {
            throw new WrongUserDataException(Message.ERROR_USER_EXIST);
        }
    }

    private boolean contains(User newUser) {
        User oldUser = userDAO.getByEmail(newUser.getEmail());
        return oldUser != null;
    }

    public User getUserByEmailAndPassword(String beanEmail, String beanPass) {
        try{
            return transactionManager.transact(() -> {
                User user = userDAO.getByEmail(beanEmail);
                checkForNull(user);
                checkForMatching(beanPass, user.getPassword());
                return user;
            });
        } catch (DaoException e) {
            throw new ServiceException(e);
        }

    }

    private void checkForMatching(String pass, String userFromDBPass) {
        if (!userFromDBPass.equals(pass)) {
            throw new WrongUserDataException(Message.ERROR_WRONG_PASS);
        }
    }

    private void checkForNull(User user) {
        if (user == null) {
            throw new WrongUserDataException(Message.ERROR_ABSENCE);
        }
    }

}
