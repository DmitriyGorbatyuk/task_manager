package ua.khai.gorbatiuk.taskmanager.dao;


import ua.khai.gorbatiuk.taskmanager.entity.model.User;

public interface UserDao {

    User add(User user);

    User getByEmail(String email);
}
