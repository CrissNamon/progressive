package ru.danilarassokhin.progressive.exception;

/**
 * Thrown if bean is not presented in DI container
 */
public class BeanNotFoundException extends Exception{
    public BeanNotFoundException(String message) {
        super(message);
    }
}
