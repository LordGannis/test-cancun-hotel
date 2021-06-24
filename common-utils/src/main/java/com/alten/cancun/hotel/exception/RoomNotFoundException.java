package com.alten.cancun.hotel.exception;

public class RoomNotFoundException extends RuntimeException {

    public RoomNotFoundException(String errorMessage) {
        super(errorMessage);
    }

}
