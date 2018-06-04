package ua.khai.gorbatiuk.taskmanager.exception;

public class DaoException extends RuntimeException {

    public DaoException(Throwable cause) {
        super(cause);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
