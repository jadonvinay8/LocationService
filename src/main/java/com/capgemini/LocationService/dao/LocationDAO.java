package com.capgemini.LocationService.dao;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.capgemini.LocationService.entities.City;

@Repository
@EnableScan
public interface LocationDAO extends CrudRepository<City, String> {
	
	@NotNull Optional<City> findById(@NotNull String id);
	
}
