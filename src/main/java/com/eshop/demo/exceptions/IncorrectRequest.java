package com.eshop.demo.exceptions;

public class IncorrectRequest extends RuntimeException{

    public IncorrectRequest() {
        super("Incorrect request.");
    }

    public IncorrectRequest(String message) {
        super("Incorrect request. Message: " + message);
    }

}
