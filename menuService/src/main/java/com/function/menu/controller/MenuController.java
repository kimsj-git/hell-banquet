package com.function.menu.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.function.menu.domain.Menu;
import com.function.menu.dto.MenuSaveRequestDto;
import com.function.menu.service.MenuService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/menus")
public class MenuController {

	private final MenuService menuService;

	@ApiOperation("모든 식단 리스트 조회 - Read 테스트를 위한 API")
	@GetMapping("")
	public List<Menu> getAllMenus() {
		return menuService.getAllMenus();
	}

	@ApiOperation(value = "{id}에 해당하는 식단 단건 조회")
	@GetMapping("/{id}")
	public Menu getMenuById(@PathVariable Long id) {
		return menuService.findMenuById(id);
	}

	@ApiOperation("{managerId}에 해당하는 영양사가 {date}에 작성한 식단 리스트를 조회한다.")
	@GetMapping("/date")
	public List<Menu> getMenusByManagerIdAndDate(
		@RequestParam("managerId") String managerId,
		@RequestParam("date") String date) {
		return menuService.getMenusByManagerIdAndDate(managerId, date);
	}

	@ApiOperation("{managerId}에 해당하는 영양사가 {date}에 작성한 식단 리스트 중 {type}에 해당하는 식단를 조회한다.")
	@GetMapping("/type")
	public Menu getMenuByManagerIdAndDateAndType(
		@RequestParam("managerId") String managerId,
		@RequestParam("date") String date,
		@RequestParam("type") String type) {
		return menuService.getMenusByManagerIdAndDateAndType(managerId, date, type);
	}

	@ApiOperation("식단 생성")
	@PostMapping()
	public Menu createMenu(@RequestBody MenuSaveRequestDto menu) {
		return menuService.save(menu);
	}

	@ApiOperation("{id}에 해당하는 식단 삭제")
	@DeleteMapping("/{id}")
	public void deleteMenu(@PathVariable Long id) {
		menuService.deleteMenu(id);
	}

}
