package ua.khai.gorbatiuk.taskmanager.exception;

public class CriticalUserDataException extends RuntimeException {
    public CriticalUserDataException(String message) {
        super(message);
    }
}
