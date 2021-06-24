package com.alten.cancun.hotel.advice;

import com.alten.cancun.hotel.exception.BookNotFoundException;
import com.alten.cancun.hotel.exception.InvalidRangeDatesException;
import com.alten.cancun.hotel.exception.NoAvailableDatesException;
import com.alten.cancun.hotel.exception.RoomNotFoundException;
import com.alten.cancun.hotel.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity handleUserNotFoundException(RoomNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(NoAvailableDatesException.class)
    public ResponseEntity handleUserNotFoundException(NoAvailableDatesException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(InvalidRangeDatesException.class)
    public ResponseEntity handleUserNotFoundException(InvalidRangeDatesException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity handleUserNotFoundException(BookNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("One or more parameters sent is wrong.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There was a internal error to complete the request");
    }

}
