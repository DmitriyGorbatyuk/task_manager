package ua.khai.gorbatiuk.taskmanager.util.converter;


import ua.khai.gorbatiuk.taskmanager.exception.ConverterException;

public interface Converter<S, T> {
    T convert(S source) throws ConverterException;
}
