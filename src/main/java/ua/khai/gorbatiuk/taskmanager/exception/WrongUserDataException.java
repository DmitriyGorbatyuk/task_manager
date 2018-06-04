package ua.khai.gorbatiuk.taskmanager.exception;

public class WrongUserDataException extends ServiceException{

    public WrongUserDataException(String message) {
        super(message);
    }
}
