package com.capgemini.LocationService.controllers;


import com.capgemini.LocationService.dto.MicroserviceResponse;
import com.capgemini.LocationService.entities.City;
import com.capgemini.LocationService.services.LocationService;
import com.capgemini.LocationService.utilities.CSVConverter;
import com.capgemini.LocationService.utilities.ResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

/**
 * Controller having End-points related to location
 *
 * @author Vinay Pratap Singh
 *
 */
@RestController
@RequestMapping("v1/cities")
@CrossOrigin
public class LocationController {

    private final LocationService locationService;
    private static final String SUCCESS_MESSAGE = "Operation Completed successfully";

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public ResponseEntity<MicroserviceResponse> getAllCities() {
        MicroserviceResponse response = ResponseBuilder.build(HttpStatus.OK.value(), locationService.getAllCities(), null);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<MicroserviceResponse> addCity(@Valid @RequestBody City city) {
        MicroserviceResponse response = ResponseBuilder.build(HttpStatus.CREATED.value(), locationService.addCity(city), null);
        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<MicroserviceResponse> updateCity(@PathVariable("id") String id, @Valid @RequestBody City city) {
        MicroserviceResponse response = ResponseBuilder.build(HttpStatus.OK.value(), locationService.updateCity(id, city), null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<MicroserviceResponse> findCityById(@PathVariable("id") String id) {
        MicroserviceResponse response = ResponseBuilder.build(HttpStatus.OK.value(), locationService.findById(id), null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MicroserviceResponse> deleteCity(@PathVariable("id") String id) {
        locationService.deleteCity(id);
        MicroserviceResponse response = ResponseBuilder.build(HttpStatus.NO_CONTENT.value(), SUCCESS_MESSAGE, null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/batch")
    public ResponseEntity<MicroserviceResponse> addMultipleCities(@RequestBody List<@NotNull String> cities) {
        locationService.addMultipleCities(cities);
        MicroserviceResponse response = ResponseBuilder.build(HttpStatus.CREATED.value(), SUCCESS_MESSAGE, null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/existence")
    public ResponseEntity<MicroserviceResponse> checkBatchExistence(@RequestBody List<@NotNull String> cityIds) {
        MicroserviceResponse response = ResponseBuilder.build(HttpStatus.OK.value(), locationService.validateBatchExistence(cityIds), null);
        return ResponseEntity.ok(response);
    }

	@PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<MicroserviceResponse> addCitiesViaFile(@RequestParam("file") MultipartFile file) throws IOException {
		var cities = CSVConverter.csvToCities(file.getInputStream());
		locationService.addMultipleCities(cities);
        MicroserviceResponse response = ResponseBuilder.build(HttpStatus.CREATED.value(), SUCCESS_MESSAGE, null);
        return ResponseEntity.ok(response);	}

}
