package com.capgemini.LocationService.services;

import com.capgemini.LocationService.entities.City;
import com.capgemini.LocationService.exceptions.CityAlreadyExistException;
import com.capgemini.LocationService.exceptions.CityNotFoundException;

import java.util.List;
import java.util.Map;

public interface LocationService {

    List<City> getAllCities();

    City addCity(City city) throws CityAlreadyExistException;

    City updateCity(String id, City city) throws CityAlreadyExistException, CityNotFoundException;

    City findById(String id) throws CityNotFoundException;

    void deleteCity(String id) throws CityNotFoundException;

    void addMultipleCities(List<String> cities) throws CityAlreadyExistException, NullPointerException;

    Map<String, String> validateBatchExistence(List<String> cityIds) throws NullPointerException;
}
