package ua.khai.gorbatiuk.taskmanager.service;


import ua.khai.gorbatiuk.taskmanager.entity.model.User;

public interface UserService {
    User register(User user);
    User getUserByEmailAndPassword(String beanLogin, String beanPass);
}
