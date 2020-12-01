package com.capgemini.LocationService.services;

import com.capgemini.LocationService.dao.LocationDAO;
import com.capgemini.LocationService.entities.City;
import com.capgemini.LocationService.exceptions.CityAlreadyExistException;
import com.capgemini.LocationService.exceptions.CityNotFoundException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ServiceLayerTest {

    @Mock
    private LocationDAO locationDAO;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private LocationServiceImpl service;

    private static Map<String, City> cityMap = new HashMap<>();

    @BeforeClass
    public static void init() {
        cityMap.put("1", new City("1", "Pune"));
        cityMap.put("2", new City("2", "Delhi"));
        cityMap.put("3", new City("3", "Mumbai"));
        cityMap.put("4", new City("4", "Bangalore"));
    }

    @Test
    public void testGetAllCities() {
        List<City> cities = new ArrayList<>();
        cityMap.forEach((key, value) -> cities.add(value));
        when(locationDAO.findAll()).thenReturn(cities);
        assertEquals(cities, service.getAllCities());
    }

    @Test
    public void testAddCity() {
        List<City> cities = new ArrayList<>();
        cityMap.forEach((key, value) -> cities.add(value));
        City city = new City("Kolkata");
        City addedCity = new City("5", "Kolkata");
        when(locationDAO.findAll()).thenReturn(cities);
        when(locationDAO.save(city)).thenReturn(addedCity);
        assertEquals(addedCity, service.addCity(city));
    }

    @Test(expected = CityAlreadyExistException.class)
    public void testAddCityThrowsCityAlreadyExistException() {
        List<City> cities = new ArrayList<>();
        cityMap.forEach((key, value) -> cities.add(value));
        City city = new City("Pune");
        City addedCity = new City("5", "Pune");
        when(locationDAO.findAll()).thenReturn(cities);
        when(locationDAO.save(city)).thenReturn(addedCity);
        assertEquals(addedCity, service.addCity(city));
    }

    @Test
    public void testFindById() {
        String id = "1";
        when(locationDAO.findById(id)).thenReturn(Optional.of(cityMap.get(id)));
        assertEquals(cityMap.get(id), service.findById(id));
    }

    @Test(expected = CityNotFoundException.class)
    public void testFindByIdThrowsCityNotFoundException() {
        String id = "7";
        when(locationDAO.findById(id)).thenReturn(Optional.ofNullable(cityMap.get(id)));
        assertEquals(cityMap.get(id), service.findById(id));
    }

    @Test
    public void testUpdateCity() {
        String id = "2";
        List<City> cities = new ArrayList<>();
        cityMap.forEach((key, value) -> cities.add(value));
        City city = new City("New Delhi");
        City addedCity = new City(id, "New Delhi");
        when(locationDAO.findById(id)).thenReturn(Optional.of(cityMap.get(id)));
        when(locationDAO.findAll()).thenReturn(cities);
        when(locationDAO.save(city)).thenReturn(addedCity);
        assertEquals(addedCity, service.updateCity(id, city));
    }

    @Test(expected = CityAlreadyExistException.class)
    public void testUpdateCityThrowsCityAlreadyExistException() {
        String id = "2";
        List<City> cities = new ArrayList<>();
        cityMap.forEach((key, value) -> cities.add(value));
        City city = new City("Delhi");
        City addedCity = new City(id, "Delhi");
        when(locationDAO.findById(id)).thenReturn(Optional.of(cityMap.get(id)));
        when(locationDAO.findAll()).thenReturn(cities);
        when(locationDAO.save(city)).thenReturn(addedCity);
        assertEquals(addedCity, service.updateCity(id, city));
    }

    @Test(expected = CityNotFoundException.class)
    public void testUpdateCityThrowsCityNotFoundException() {
        String id = "6";
        List<City> cities = new ArrayList<>();
        cityMap.forEach((key, value) -> cities.add(value));
        City city = new City("New Delhi");
        City addedCity = new City(id, "New Delhi");
        when(locationDAO.findById(id)).thenReturn(Optional.ofNullable(cityMap.get(id)));
        when(locationDAO.findAll()).thenReturn(cities);
        when(locationDAO.save(city)).thenReturn(addedCity);
        assertEquals(addedCity, service.updateCity(id, city));
    }

    @Test
    public void testAddMultipleCities() {
        List<City> cities = new ArrayList<>();
        cityMap.forEach((key, value) -> cities.add(value));
        when(locationDAO.findAll()).thenReturn(cities);
        List<City> citiesToBeAdded = List.of(new City("Ghaziabad"), new City("Noida"));
        List<City> addedCities = List.of(new City("10", "Ghaziabad"), new City("11", "Noida"));
        List<String> namesToBeAdded = List.of("Ghaziabad", "Noida");
        when(locationDAO.saveAll(citiesToBeAdded)).thenReturn(addedCities);
        addedCities.forEach(city -> cityMap.put(city.getId(), city));
        service.addMultipleCities(namesToBeAdded);
        assertEquals(6, cityMap.size());
    }

    @Test(expected = CityAlreadyExistException.class)
    public void testAddMultipleCitiesThrowsCityAlreadyExistException() {
        List<City> cities = new ArrayList<>();
        cityMap.forEach((key, value) -> cities.add(value));
        when(locationDAO.findAll()).thenReturn(cities);
        List<City> citiesToBeAdded = List.of(new City("Gurgaon"), new City("Delhi"));
        List<City> addedCities = List.of(new City("10", "Gurgaon"), new City("11", "Delhi"));
        List<String> namesToBeAdded = List.of("Gurgaon", "Delhi");
        when(locationDAO.saveAll(citiesToBeAdded)).thenReturn(addedCities);
        service.addMultipleCities(namesToBeAdded);
        assertEquals(6, cityMap.size());
    }

    @Test
    public void testDeleteCity() {
        String id = "2";
        doNothing().when(restTemplate).delete(anyString());
        doNothing().when(locationDAO).delete(new City("2", "Delhi"));
        when(locationDAO.findById(id)).thenReturn(Optional.of(cityMap.get(id)));
        ReflectionTestUtils.setField(service, "theaterRemovalUrl", id);
        service.deleteCity(id);
        cityMap.remove(id);
        assertEquals(5, cityMap.size());
    }

    @Test
    public void testValidateBatchExistence() {
//        when(locationDAO.findById(anyString())).thenReturn(Optional.ofNullable(cityMap.get(anyString())));
        when(locationDAO.findById("1")).thenReturn(Optional.ofNullable(cityMap.get("1")));
        when(locationDAO.findById("2")).thenReturn(Optional.ofNullable(cityMap.get("2")));
        when(locationDAO.findById("7")).thenReturn(Optional.ofNullable(cityMap.get("7")));
        List<String> cityIds = List.of("1", "2", "7");
        Map<String, String> map = new HashMap<>();
        map.put("1", "Pune");
        map.put("2", "Delhi");
        map.put("Invalid", List.of("7").toString());
        assertEquals(map, service.validateBatchExistence(cityIds));
    }

    @Test(expected = NullPointerException.class)
    public void testValidateBatchExistenceThrowsNullPointerException() {
        when(locationDAO.findById("1")).thenReturn(Optional.ofNullable(cityMap.get("1")));
        when(locationDAO.findById("2")).thenReturn(Optional.ofNullable(cityMap.get("2")));
        when(locationDAO.findById("7")).thenReturn(Optional.ofNullable(cityMap.get("7")));
        List<String> cityIds = null;
        Map<String, String> map = new HashMap<>();
        map.put("1", "Pune");
        map.put("2", "Delhi");
        map.put("Invalid", List.of("7").toString());
        assertEquals(map, service.validateBatchExistence(cityIds));
    }

}
