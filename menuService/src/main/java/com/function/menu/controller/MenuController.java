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
@RequestMapping("/meals")
public class MenuController {

	private final MenuService menuService;

	@GetMapping("")
	public List<Menu> getAllMeals() {
		return menuService.getAllMenus();
	}

	@ApiOperation("{managerId}를 갖는 영양사가 {date}에 작성한 식단 리스트")
	@GetMapping("/detail/{managerId}")
	public List<Menu> getMealByManagerIdAndDate(@PathVariable String managerId,
		@RequestParam("date") String date) {
		return menuService.getMenusByManagerIdAndDate(managerId, date);
	}

	@ApiOperation("{managerId}를 갖는 영양사가 {date}에 작성한 식단 리스트")
	@GetMapping("/detail/type/{managerId}")
	public Menu getMealByManagerIdAndDateAndType(@PathVariable String managerId,
		@RequestParam("date") String date,
		@RequestParam("type") String type) {
		return menuService.getMenusByManagerIdAndDateAndType(managerId, date, type);
	}

	@GetMapping("/{id}")
	public Menu getMealById(@PathVariable Long id) {
		return menuService.findMenuById(id);
	}

	@GetMapping("/manager/{managerId}")
	public List<Menu> getMealByManagerId(@PathVariable String managerId) {
		return menuService.findMenuByManagerId(managerId);
	}

	@PostMapping("")
	public Menu createMeal(@RequestBody MenuSaveRequestDto menu) {
		return menuService.save(menu);
	}

	@DeleteMapping("/{id}")
	public void deleteMenu(@PathVariable Long id) {
		menuService.deleteMenu(id);
	}

}
