package com.eshop.demo.exceptions;

public class UnprocessableRequest extends RuntimeException {

    public UnprocessableRequest() {
        super("Unprocessable request.");
    }

    public UnprocessableRequest(String message) {
        super("Unprocessable request. Message: " + message);
    }

}
