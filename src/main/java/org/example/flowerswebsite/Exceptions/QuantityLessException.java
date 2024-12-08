package org.example.flowerswebsite.Exceptions;

public class QuantityLessException extends RuntimeException{
    public QuantityLessException(String message){
        super(message);
    }
}
