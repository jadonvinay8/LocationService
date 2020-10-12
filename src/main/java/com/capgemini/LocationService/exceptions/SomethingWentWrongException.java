package com.capgemini.LocationService.exceptions;

public class SomethingWentWrongException extends RuntimeException {

    public SomethingWentWrongException() {
    }

    public SomethingWentWrongException(String s) {
        super(s);
    }
}
