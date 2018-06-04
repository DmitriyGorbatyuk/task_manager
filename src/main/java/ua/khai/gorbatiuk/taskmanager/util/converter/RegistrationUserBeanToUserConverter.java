package ua.khai.gorbatiuk.taskmanager.util.converter;


import ua.khai.gorbatiuk.taskmanager.entity.bean.RegistrationUserBean;
import ua.khai.gorbatiuk.taskmanager.entity.model.User;

public class RegistrationUserBeanToUserConverter implements Converter<RegistrationUserBean, User> {
    @Override
    public User convert(RegistrationUserBean source) {
        return new User(source.getId(),
                source.getEmail(),
                source.getPassword());
    }
}
