package ua.khai.gorbatiuk.taskmanager.util.converter.populator;


import ua.khai.gorbatiuk.taskmanager.exception.PopulatorException;

public interface Populator<S, T> {
    void populate(S source, T target) throws PopulatorException;
}
