package com.function.menu.service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.function.menu.domain.Menu;
import com.function.menu.domain.MenuRepository;
import com.function.menu.domain.Sequence;
import com.function.menu.dto.MenuSaveRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuService {

	private final MenuRepository menuRepository;
	private final MongoOperations mongo;

	private long getNextSequence(String sequenceName, MongoOperations mongo) {
		Query query = new Query(Criteria.where("_id").is(sequenceName));
		Update update = new Update().inc("seq", 1);
		Sequence sequence = mongo.findAndModify(query, update, options().returnNew(true).upsert(true), Sequence.class);
		return sequence.getSeq();
	}

	@Transactional
	public Menu save(MenuSaveRequestDto dto) {
		long id = getNextSequence("menu_sequence", mongo);
		LocalDate date = parseDate(dto.getDate());
		Menu menu = Menu.builder()
			.id(id)
			.dto(dto)
			.date(date)
			.build();
		return menuRepository.save(menu);
	}

	@Transactional(readOnly = true)
	public List<Menu> getAllMenus() {
		return menuRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Menu findMenuById(long id) {
		return menuRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public List<Menu> findMenuByManagerId(String id) {
		return menuRepository.findMenuByManagerId(id);
	}

	@Transactional
	public void deleteMenu(Long id) {
		menuRepository.deleteById(id);
	}

	public List<Menu> getMenusByManagerIdAndDate(String managerId, String date) {
		return menuRepository.findByManagerIdAndDate(managerId, parseDate(date));
	}

	public Menu getMenusByManagerIdAndDateAndType(String managerId, String date, String type) {
		return menuRepository.findByManagerIdAndDateAndType(managerId, parseDate(date), type);
	}

	public LocalDate parseDate(String dateStr) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return LocalDate.parse(dateStr, formatter);
	}

}
