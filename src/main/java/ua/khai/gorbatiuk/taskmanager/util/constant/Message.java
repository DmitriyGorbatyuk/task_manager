package ua.khai.gorbatiuk.taskmanager.util.constant;

public class Message {

    public static final String ERROR_EMPTY_FIELD = "Fill this field, please ";

    public static final String ERROR_EMAIL = "There is invalid email";
    public static final String ERROR_PASS = "Password may contain only latin symbols(A-Z), numbers and underscore between ";
    public static final String ERROR_CONFIRMED_PASS = "Passwords don't match";
    public static final String ERROR_USER_EXIST = "User with such email already exists";
    public static final String ERROR_ABSENCE = "There is no user with such email";
    public static final String ERROR_WRONG_PASS = "Wrong password";

    public static final String SUCCESS_REGISTRATION = "You have registered!";

    private Message() {}
}
