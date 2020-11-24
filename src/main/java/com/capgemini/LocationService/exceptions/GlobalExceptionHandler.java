package com.capgemini.LocationService.exceptions;

import com.capgemini.LocationService.dto.ErrorResponse;
import com.capgemini.LocationService.dto.MicroserviceResponse;
import com.capgemini.LocationService.utilities.ResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CityNotFoundException.class)
    public ResponseEntity<MicroserviceResponse> cityNotFound(CityNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse("No City was found with given id", e.getMessage(), "404");
        MicroserviceResponse response = ResponseBuilder.build(HttpStatus.NOT_FOUND.value(), null, errorResponse);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<MicroserviceResponse> nullPointer(NullPointerException e) {
        ErrorResponse errorResponse = new ErrorResponse("Nulls are not allowed", e.getMessage(), "400");
        MicroserviceResponse response = ResponseBuilder.build(HttpStatus.BAD_REQUEST.value(), null, errorResponse);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MicroserviceResponse> invalidInputPayload(MethodArgumentNotValidException e) {
        ErrorResponse errorResponse = new ErrorResponse("Invalid Payload Supplied, check for nulls/blanks", e.getMessage(), "400");
        MicroserviceResponse response = ResponseBuilder.build(HttpStatus.BAD_REQUEST.value(), null, errorResponse);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(CityAlreadyExistException.class)
    public ResponseEntity<MicroserviceResponse> cityAlreadyExists(CityAlreadyExistException e) {
        ErrorResponse errorResponse = new ErrorResponse("City Already exists", e.getMessage(), "400");
        MicroserviceResponse response = ResponseBuilder.build(HttpStatus.BAD_REQUEST.value(), null, errorResponse);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<MicroserviceResponse> cityAlreadyExists(HttpMessageNotReadableException e) {
        ErrorResponse errorResponse = new ErrorResponse("Invalid request body supplied", e.getMessage(), "400");
        MicroserviceResponse response = ResponseBuilder.build(HttpStatus.BAD_REQUEST.value(), null, errorResponse);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(OperationFailedException.class)
    public ResponseEntity<MicroserviceResponse> operationFailed(OperationFailedException e) {
        ErrorResponse errorResponse = new ErrorResponse("Something went wrong in companion service", e.getMessage(), "500");
        MicroserviceResponse response = ResponseBuilder.build(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, errorResponse);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MicroserviceResponse> anyUnhandledException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse("Some unknown error occurred", e.getMessage(), "500");
        MicroserviceResponse response = ResponseBuilder.build(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, errorResponse);
        return ResponseEntity.ok(response);
    }

}