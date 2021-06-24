package com.alten.cancun.hotel.exception;

public class NoAvailableDatesException extends RuntimeException {

    public NoAvailableDatesException(String errorMessage) {
        super(errorMessage);
    }

}
