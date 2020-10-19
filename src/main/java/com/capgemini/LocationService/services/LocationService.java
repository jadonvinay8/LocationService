package com.capgemini.LocationService.services;


import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.capgemini.LocationService.dao.LocationDAO;
import com.capgemini.LocationService.exceptions.CityAlreadyExistException;
import com.capgemini.LocationService.exceptions.CityNotFoundException;
import com.capgemini.LocationService.exceptions.OperationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.capgemini.LocationService.entities.City;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Service class to perform CRUD operations related to Location functionality
 * 
 * @author Vinay Pratap Singh
 *
 */
@Service
@Transactional
public class LocationService {

	private final LocationDAO locationDAO;
	private final RestTemplate restTemplate;

	@Value("${service.theater.removal}")
	private String theaterRemovalUrl;

	@Autowired
	public LocationService(LocationDAO locationDAO, RestTemplate restTemplate) {
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
		if (getCityNames().contains(city.toLowerCase()))
			throw new CityAlreadyExistException("City with name " + city + " already exists");
	}

	private void validateUniqueConstraint(List<String> cities) {
		var existentCities = getCityNames();
		cities.stream()
				.map(String::toLowerCase)
				.filter(existentCities::contains)
				.forEach(city -> {
					throw new CityAlreadyExistException("City with name " + city + " already exists");
				});
	}
	public City addCity(City city) {
		validateUniqueConstraint(city.getCityName()); // throws exception if invalid
		return locationDAO.save(city);
	}

	public City findById(String id) {
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

		try {
			restTemplate.delete(requestUrl); // delete theaters in this city by calling theater API
			locationDAO.delete(city);
		} catch (HttpClientErrorException e) {
			throw new OperationFailedException("Could not delete the underlying theaters, hence terminating the operation");
		}
	}

	public void addMultipleCities(List<String> cities) {
		validateUniqueConstraint(cities); // throws exception if invalid

		Set<City> filteredCities = validateInputList(cities)
				.stream()
				.filter(Objects::nonNull)
				.map(City::new)
				.collect(Collectors.toSet());

		locationDAO.saveAll(filteredCities);
	}

	public boolean validateBatchExistence(List<String> cityIds) {
		validateInputList(cityIds).forEach(this::findById);
		return true;
	}

	private List<String> validateInputList(List<String> list) {
		 return Optional.ofNullable(list).orElseThrow(() -> {
			throw new NullPointerException("Null value supplied in payload");
		});
	}


}
