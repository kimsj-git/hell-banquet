package com.function.menu.service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.function.menu.domain.Menu;
import com.function.menu.domain.MenuRepository;
import com.function.menu.domain.Sequence;
import com.function.menu.dto.ExcelizedMenuRegisterRequestDto;
import com.function.menu.dto.ExcelizedMenuRegisterResultDto;
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
			.managerId(dto.getManagerId())
			.date(date)
			.type(dto.getType())
			.category(dto.getCategory())
			.feature(dto.getFeature())
			.menuItems(dto.getMenuItems())
			.menuTypes(dto.getMenuTypes())
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

	@Transactional
	public void deleteMenu(Long id) {
		menuRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public List<Menu> getMenusByManagerIdAndDate(String managerId, String date) {
		return menuRepository.findByManagerIdAndDate(managerId, parseDate(date));
	}

	@Transactional(readOnly = true)
	public Menu getMenusByManagerIdAndDateAndType(String managerId, String date, String type) {
		return menuRepository.findByManagerIdAndDateAndType(managerId, parseDate(date), type);
	}

	@Transactional
	public List<ExcelizedMenuRegisterResultDto> createAllMenus(Workbook requestWorkbook) {
		Sheet requestSheet = requestWorkbook.getSheetAt(0);
		List<ExcelizedMenuRegisterResultDto> resultList = new ArrayList<>();

		for (int i = 1; i < requestSheet.getPhysicalNumberOfRows(); i++) {
			Row requestRow = requestSheet.getRow(i);
			ExcelizedMenuRegisterResultDto resultDto = new ExcelizedMenuRegisterResultDto(i);
			setResultDto(resultDto, requestRow);

			long id = getNextSequence("menu_sequence", mongo);
			menuRepository.save(createMenuFromResultDto(resultDto, id));

			resultList.add(resultDto);
		}
		return resultList;

	}

	private void setResultDto(ExcelizedMenuRegisterResultDto resultDto, Row requestRow) {
		resultDto.setManagerId(getStringCellValue(requestRow, 1));
		resultDto.setType(getStringCellValue(requestRow, 2));
		resultDto.setCategory(getStringCellValue(requestRow, 3));
		resultDto.setFeature(getStringCellValue(requestRow, 4));
		resultDto.setMenuItems(getListFromString(getStringCellValue(requestRow, 5)));
		resultDto.setMenuTypes(getListFromString(getStringCellValue(requestRow, 6)));
		resultDto.setDate(parseDate(String.valueOf((int)getNumericCellValue(requestRow, 7))));
	}

	public LocalDate parseDate(String dateStr) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
		return LocalDate.parse(dateStr, formatter);
	}

	private String getStringCellValue(Row row, int cellIndex) {
		return row.getCell(cellIndex).getStringCellValue();
	}

	private double getNumericCellValue(Row row, int cellIndex) {
		return row.getCell(cellIndex).getNumericCellValue();
	}

	private List<String> getListFromString(String string) {
		return List.of(string.split(", "));
	}

	private Menu createMenuFromResultDto(ExcelizedMenuRegisterResultDto resultDto, long id) {
		return Menu.builder()
			.id(id)
			.managerId(resultDto.getManagerId())
			.date(resultDto.getDate())
			.type(resultDto.getType())
			.category(resultDto.getCategory())
			.feature(resultDto.getFeature())
			.menuItems(resultDto.getMenuItems())
			.menuTypes(resultDto.getMenuTypes())
			.build();
	}

	public boolean listToExcel(List<ExcelizedMenuRegisterRequestDto> list, HttpServletResponse response) {
		Workbook resultWorkbook = new SXSSFWorkbook();
		Sheet resultSheet = resultWorkbook.createSheet("등록 결과");
		resultSheet.setColumnWidth(1, 5000);
		resultSheet.setColumnWidth(5, 7000);
		resultSheet.setColumnWidth(6, 7000);
		resultSheet.setColumnWidth(7, 5000);

		Row header = resultSheet.createRow(0);
		String[] headers = {"요청 번호", "영양사 아이디", "타입", "카테고리", "특징", "메뉴 이름 리스트", "메뉴 타입 리스트", "식단 날짜"};

		for (int i = 0; i < headers.length; i++) {
			Cell headerCell = header.createCell(i);
			headerCell.setCellValue(headers[i]);
		}

		for (int i = 0; i < list.size(); i++) {
			Row resultRow = resultSheet.createRow(i + 1);
			ExcelizedMenuRegisterRequestDto menuRegisterRequestDto = list.get(i);

			resultRow.createCell(0).setCellValue(i + 1);
			resultRow.createCell(1).setCellValue(menuRegisterRequestDto.getManagerId());
			resultRow.createCell(2).setCellValue(menuRegisterRequestDto.getType());
			resultRow.createCell(3).setCellValue(menuRegisterRequestDto.getCategory());
			resultRow.createCell(4).setCellValue(menuRegisterRequestDto.getFeature());
			resultRow.createCell(5).setCellValue(menuRegisterRequestDto.getMenuItems().toString());
			resultRow.createCell(6).setCellValue(menuRegisterRequestDto.getMenuTypes().toString());
			resultRow.createCell(7).setCellValue(menuRegisterRequestDto.getDate());
		}

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment;filename=result.xlsx");

		try (ServletOutputStream outputStream = response.getOutputStream()) {
			resultWorkbook.write(outputStream);
			resultWorkbook.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("결과 파일 작성 중 오류가 발생했습니다. (IOException)");
		}
	}

}
