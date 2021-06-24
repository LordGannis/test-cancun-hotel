package com.alten.cancun.hotel.exception;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(String errorMessage) {
        super(errorMessage);
    }

}
