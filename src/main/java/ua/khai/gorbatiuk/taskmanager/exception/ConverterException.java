package ua.khai.gorbatiuk.taskmanager.exception;

public class ConverterException extends RuntimeException{

    public ConverterException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConverterException(Throwable cause) {
        super(cause);
    }

    public ConverterException(String message) {
        super(message);
    }
}
