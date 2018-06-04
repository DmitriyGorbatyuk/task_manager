package ua.khai.gorbatiuk.taskmanager.util.validator;

import ua.khai.gorbatiuk.taskmanager.entity.bean.LoginUserBean;
import ua.khai.gorbatiuk.taskmanager.util.constant.Message;
import ua.khai.gorbatiuk.taskmanager.util.constant.MessageKey;
import ua.khai.gorbatiuk.taskmanager.util.validator.secirity.Security;

import java.util.HashMap;
import java.util.Map;

public class LoginUserBeanValidator {
    private static final String AND = " and ";

    private static final int MIN_USER_PASS_LENGTH = 5;
    private static final int MAX_USER_PASS_LENGTH = 20;

    private static final String REGEX_PASS = "^[A-Za-z_0-9]{" + MIN_USER_PASS_LENGTH + ',' + MAX_USER_PASS_LENGTH + "}$";
    private static final String REGEX_EMAIL =
            "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08" +
                    "\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")" +
                    "@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4]" +
                    "[0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01" +
                    "-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    private Security security;

    public LoginUserBeanValidator(Security security) {
        this.security = security;
    }

    public Map<String, String> validate(LoginUserBean loginUserBean) {
        Map<String, String> errors = new HashMap<>();

        validateUserEmail(loginUserBean, errors);
        validateUserPassword(loginUserBean, errors);

        return errors;
    }

    private boolean isEmpty(String name) {
        return name == null || name.equals("");
    }

    public void validateUserEmail(LoginUserBean bean, Map<String, String> errors) {
        String login = bean.getEmail();

        if (isEmpty(login)) {
            errors.put(MessageKey.ERROR_EMAIL, Message.ERROR_EMPTY_FIELD);
            return;
        }
        if (!login.matches(REGEX_EMAIL)) {
            errors.put(MessageKey.ERROR_EMAIL, Message.ERROR_EMAIL);
        }
    }

    public void validateUserPassword(LoginUserBean bean, Map<String, String> errors) {
        String password = bean.getPassword();

        if (isEmpty(password)) {
            errors.put(MessageKey.ERROR_PASS, Message.ERROR_EMPTY_FIELD);
            return;
        }

        if (!password.matches(REGEX_PASS)) {
            errors.put(MessageKey.ERROR_PASS, Message.ERROR_PASS + MIN_USER_PASS_LENGTH + AND + MAX_USER_PASS_LENGTH);
            return;
        }
        bean.setPassword(security.hash(bean.getPassword()));
    }
}
