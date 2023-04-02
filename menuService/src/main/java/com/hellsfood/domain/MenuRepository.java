package com.hellsfood.domain;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface MenuRepository extends MongoRepository<Menu, Long> {

	Menu findById(long id);
	void deleteById(Long id);
	List<Menu> findByManagerIdAndDate(String managerId, LocalDate date);
	Menu findByManagerIdAndDateAndType(String managerId, LocalDate parseDate, String type);

	long countByDateAndManagerId(LocalDate time, String managerId);
	List<Menu> findByDate(LocalDate date);

	@Query("{'date': ?0, 'managerId': ?1, 'type': {$ne: ?2}}")
	List<Menu> findByDateAndManagerIdAndNotType(LocalDate date, String managerId, String type);

}

