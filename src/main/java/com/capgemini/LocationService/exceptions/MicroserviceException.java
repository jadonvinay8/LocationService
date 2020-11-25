package com.capgemini.LocationService.exceptions;

import com.capgemini.LocationService.dto.ErrorResponse;

public class MicroserviceException extends RuntimeException{

    private final ErrorResponse errorResponse;

    public MicroserviceException(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}