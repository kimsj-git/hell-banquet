package com.function.menu.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.function.menu.domain.Menu;
import com.function.menu.dto.ExcelizedMenuRegisterResultDto;
import com.function.menu.dto.MenuSaveRequestDto;
import com.function.menu.service.MenuService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

	@PostMapping("/all")
	@ApiOperation(value = "일괄 식단 등록 처리", notes = "엑셀 파일로 저장된 메뉴 정보를 기반으로 순차적으로 식단 등록을 진행한다.")
	public ResponseEntity createAllMenus(
		@RequestParam("file") @ApiParam(value = "일괄 식단 요청 정보 xlsx 파일", required = true) MultipartFile file) {

		String extension = FilenameUtils.getExtension(file.getOriginalFilename());

		if (!extension.contains("xls")) {
			return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("지원되는 엑셀 파일 확장자가 아닙니다.");
		}

		Workbook workbook = null;
		InputStream inputStream = null;

		try {
			inputStream = file.getInputStream();
			if (extension.equals("xlsx")) {
				workbook = new XSSFWorkbook(inputStream);
			} else if (extension.equals("xls")) {
				workbook = new HSSFWorkbook(inputStream);
			}

			List<ExcelizedMenuRegisterResultDto> resultList = menuService.createAllMenus(workbook);
			if (resultList != null) {
				return ResponseEntity.ok(resultList);
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("가입 처리중 오류가 발생했습니다.");
			}
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("엑셀 파일을 읽는 중 오류가 발생하였습니다.");
		} finally {
			try {
				if (workbook != null) {
					workbook.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
				// 삭제하려는 파일을 다시 닫습니다.
				file.getInputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// @PostMapping("/convert")
	// @ApiOperation(value = "일괄 회원가입 결과 리스트를 xlsx 파일 변환", notes = "웹 페이지상에 보여지고 있는 일괄 회원가입 결과 리스트를 엑셀파일(xlsx)로 변환해준다.")
	// public ResponseEntity listToExcel(
	// 	@RequestBody @ApiParam(value = "변환 요청 정보", required = true) List<ExcelizedMenuRegisterResultDto> list,
	// 	HttpServletResponse response) {
	// 	if (menuService.listToExcel(list, response)) {
	// 		return ResponseEntity.ok("리스트 변환에 성공하였습니다.");
	// 	} else {
	// 		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리스트 변환 중 오류가 발생했습니다.");
	// 	}
	// }

}
