package com.capgemini.LocationService.exceptions;

import com.capgemini.LocationService.beans.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CityNotFoundException.class)
    public ResponseEntity<ErrorResponse> cityNotFound(CityNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse("No City was found with given id",
                e.getMessage(), "404"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> nullPointer(NullPointerException e) {
        return new ResponseEntity<>(new ErrorResponse("Nulls are not allowed", e.getMessage(), "400"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> invalidInputPayload(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(new ErrorResponse("Invalid Payload Supplied, check for nulls/blanks",
                e.getMessage(), "400"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CityAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> cityAlreadyExists(CityAlreadyExistException e) {
        return new ResponseEntity<>(new ErrorResponse("City Already exists", e.getMessage(), "400"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> cityAlreadyExists(HttpMessageNotReadableException e) {
        return new ResponseEntity<>(new ErrorResponse("Invalid request body supplied", e.getMessage(),
                "400"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> anyUnhandledException(Exception e) {
        return new ResponseEntity<>(new ErrorResponse("City Already exists", e.getMessage(), "500"),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}