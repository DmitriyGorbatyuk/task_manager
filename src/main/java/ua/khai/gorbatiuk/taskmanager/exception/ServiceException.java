package ua.khai.gorbatiuk.taskmanager.exception;

public class ServiceException extends RuntimeException{

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message) {
        super(message);
    }
}
