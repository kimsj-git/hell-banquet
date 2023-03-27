package com.function.menu.domain;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MenuRepository extends MongoRepository<Menu, Long> {

	Menu findById(long id);
	void deleteById(Long id);
	List<Menu> findByManagerIdAndDate(String managerId, LocalDate date);
	Menu findByManagerIdAndDateAndType(String managerId, LocalDate parseDate, String type);

}

