package com.ordermentum.microsrc.exception;

public class InvalidInputException extends RuntimeException {

    public InvalidInputException(String message, String input){
        super(message + ": " + input);
    }

}
