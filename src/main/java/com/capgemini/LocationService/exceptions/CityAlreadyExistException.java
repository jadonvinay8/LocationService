package com.capgemini.LocationService.exceptions;

public class CityAlreadyExistException extends RuntimeException {

    public CityAlreadyExistException() { }

    public CityAlreadyExistException(String arg0) {
        super(arg0);
    }
}
