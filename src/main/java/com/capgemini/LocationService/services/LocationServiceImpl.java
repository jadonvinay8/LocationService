package com.capgemini.LocationService.services;


import com.capgemini.LocationService.dao.LocationDAO;
import com.capgemini.LocationService.entities.City;
import com.capgemini.LocationService.exceptions.CityAlreadyExistException;
import com.capgemini.LocationService.exceptions.CityNotFoundException;
import com.capgemini.LocationService.exceptions.OperationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service class to perform CRUD operations related to Location functionality
 *
 * @author Vinay Pratap Singh
 */
@Service
@Transactional
public class LocationServiceImpl implements LocationService {

    private final LocationDAO locationDAO;
    private final RestTemplate restTemplate;

    @Value("${service.theater.removal}")
    private String theaterRemovalUrl;

    @Autowired
    public LocationServiceImpl(LocationDAO locationDAO, RestTemplate restTemplate) {
        this.locationDAO = locationDAO;
        this.restTemplate = restTemplate;
    }

    public List<City> getAllCities() {
        return StreamSupport
          .stream(locationDAO.findAll().spliterator(), false)
          .collect(Collectors.toList());
    }

    private Set<String> getCityNames() {
        return this.getAllCities()
          .stream()
          .map(City::getCityName)
          .map(String::toLowerCase)
          .collect(Collectors.toSet());
    }

    private void validateUniqueConstraint(String city) {
        if (getCityNames().contains(city.trim().toLowerCase()))
            throw new CityAlreadyExistException("City with name " + city.trim() + " already exists");
    }

    private List<String> validateUniqueConstraint(List<String> cities) {
        var existentCities = getCityNames();
        List<String> invalidCities = new ArrayList<>();
        cities.stream()
          .map(String::toLowerCase)
          .map(String::trim)
          .filter(existentCities::contains)
          .forEach(invalidCities::add);
        return invalidCities;
    }

    public City addCity(City city) throws CityAlreadyExistException {
        validateUniqueConstraint(city.getCityName()); // throws exception if invalid
        city.setCityName(city.getCityName().trim());
        return locationDAO.save(city);
    }

    public City findById(String id) throws CityNotFoundException {
        return locationDAO.findById(id).orElseThrow(CityNotFoundException::new);
    }

    public City updateCity(String id, City city) {
        findById(id); // if this city didn't exist previously, an exception will be thrown
        city.setId(id);

        validateUniqueConstraint(city.getCityName()); // throws exception if invalid
        return locationDAO.save(city);
    }

    public void deleteCity(String id) {
        City city = findById(id);
        var requestUrl = theaterRemovalUrl.replaceAll("cityId", id);
        restTemplate.delete(requestUrl); // delete theaters in this city by calling theater API
        locationDAO.delete(city);
    }

    public void addMultipleCities(List<String> cities) {
        List<String> invalidCities = validateUniqueConstraint(cities);
        if (!invalidCities.isEmpty()) {
            throw new CityAlreadyExistException("These cities already exist: " + invalidCities);
        }

        Set<City> filteredCities = validateInputList(cities)
          .stream()
          .filter(Objects::nonNull)
          .map(String::trim)
          .map(City::new)
          .collect(Collectors.toSet());

        locationDAO.saveAll(filteredCities);
    }

    public Map<String, String> validateBatchExistence(List<String> cityIds) {
        Map<String, String> cityMap = new HashMap<>();
        List<String> invalidIds = new ArrayList<>();

        validateInputList(cityIds).forEach(cityId -> {
            try {
                cityMap.put(cityId, findById(cityId).getCityName());
            } catch (CityNotFoundException exception) {
                invalidIds.add(cityId);
            }
            cityMap.put("Invalid", invalidIds.toString());
        });
        return cityMap;
    }

    private List<String> validateInputList(List<String> list) {
        return Optional.ofNullable(list).orElseThrow(() -> {
            throw new NullPointerException("Null value supplied in payload");
        });
    }

}
