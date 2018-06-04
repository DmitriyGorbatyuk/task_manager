package ua.khai.gorbatiuk.taskmanager.exception;

public class PopulatorException extends RuntimeException {

    public PopulatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public PopulatorException(Throwable cause) {
        super(cause);
    }
}
