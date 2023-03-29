package com.function.menu.service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.function.menu.domain.Menu;
import com.function.menu.domain.MenuRepository;
import com.function.menu.domain.Sequence;
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

	@Transactional
	public List<ExcelizedMenuRegisterResultDto> createAllMenus(Workbook requestWorkbook) {
		Sheet requestSheet = requestWorkbook.getSheetAt(0);
		List<ExcelizedMenuRegisterResultDto> resultList = new ArrayList<>();

		for (int i = 1; i < requestSheet.getPhysicalNumberOfRows(); i++) {
			Row requestRow = requestSheet.getRow(i);
			ExcelizedMenuRegisterResultDto resultDto = new ExcelizedMenuRegisterResultDto(i);

			resultDto.setManagerId(requestRow.getCell(1).getStringCellValue());
			// System.out.println("managerId: " + resultDto.getManagerId());

			resultDto.setType(requestRow.getCell(2).getStringCellValue());
			// System.out.println("type: " + resultDto.getType());

			resultDto.setCategory(requestRow.getCell(3).getStringCellValue());
			// System.out.println("category: " + resultDto.getCategory());

			resultDto.setFeature(requestRow.getCell(4).getStringCellValue());
			// System.out.println("feature: " + resultDto.getFeature());

			resultDto.setMenuItems(List.of(requestRow.getCell(5).getStringCellValue().split(", ")));
			// System.out.println("menuItems: " + resultDto.getMenuItems());

			resultDto.setMenuTypes(List.of(requestRow.getCell(6).getStringCellValue().split(", ")));
			// System.out.println("menuTypes: " + resultDto.getMenuTypes());

			double numericCellValue = requestRow.getCell(7).getNumericCellValue();
			String dateStr = String.valueOf((int) numericCellValue);
			// System.out.println("dateStr: " + dateStr);
			LocalDate date = stringToLocalDate(dateStr);
			resultDto.setDate(date);
			// System.out.println("date: " + resultDto.getDate());

			long id = getNextSequence("menu_sequence", mongo);

			menuRepository.save(Menu.builder()
					.id(id)
					.managerId(resultDto.getManagerId())
					.date(resultDto.getDate())
					.type(resultDto.getType())
					.category(resultDto.getCategory())
					.feature(resultDto.getFeature())
					.menuItems(resultDto.getMenuItems())
					.menuTypes(resultDto.getMenuTypes())
					.build());

			resultList.add(resultDto);
		}
		return resultList;
	}

	// public boolean listToExcel(List<ExcelizedMenuRegisterResultDto> list, HttpServletResponse response) {
	// 	Workbook resultWorkbook = new SXSSFWorkbook();
	// 	Sheet resultSheet = resultWorkbook.createSheet("가입 결과");
	// 	resultSheet.setColumnWidth(1, 7500);
	// 	resultSheet.setColumnWidth(2, 4400);
	// 	resultSheet.setColumnWidth(3, 7500);
	// 	resultSheet.setColumnWidth(5, 20000);
	// 	Row header = resultSheet.createRow(0);
	// 	Cell headerCell = header.createCell(0);
	// 	headerCell.setCellValue("요청 번호");
	// 	headerCell = header.createCell(1);
	// 	headerCell.setCellValue("이메일");
	// 	headerCell = header.createCell(2);
	// 	headerCell.setCellValue("ID");
	// 	headerCell = header.createCell(3);
	// 	headerCell.setCellValue("사용자명");
	// 	headerCell = header.createCell(4);
	// 	headerCell.setCellValue("가입 결과");
	// 	headerCell = header.createCell(5);
	// 	headerCell.setCellValue("비고");
	//
	// 	for (int i = 0; i < list.size(); i++) {
	// 		Row resultRow = resultSheet.createRow(i + 1);
	//
	// 		Cell cell = resultRow.createCell(0);
	// 		cell.setCellValue(i + 1);
	// 		cell = resultRow.createCell(1);
	// 		cell.setCellValue(list.get(i).getEmail());
	// 		cell = resultRow.createCell(2);
	// 		cell.setCellValue(list.get(i).getUserId());
	// 		cell = resultRow.createCell(3);
	// 		cell.setCellValue(list.get(i).getName());
	// 		cell = resultRow.createCell(4);
	// 		cell.setCellValue(list.get(i).getResult());
	// 		cell = resultRow.createCell(5);
	// 		cell.setCellValue(list.get(i).getErrorInfo());
	// 	}
	// 	response.setContentType("application/vnd.ms-excel");
	// 	response.setHeader("Content-Disposition", "attachment;filename=result.xlsx");
	// 	// file 손상 오류 https://developer-davii.tistory.com/49
	//
	// 	try {
	// 		resultWorkbook.write(response.getOutputStream());
	// 		resultWorkbook.close();
	// 		response.getOutputStream().close();
	// 		return true;
	// 	} catch (IOException e) {
	// 		e.printStackTrace();
	// 		throw new RuntimeException("결과 파일 작성 중 오류가 발생했습니다. (IOException)");
	// 	} catch (Exception e) {
	// 		e.printStackTrace();
	// 	}
	// 	return false;
	// }

	public LocalDate stringToLocalDate(String dateStr) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		return LocalDate.parse(dateStr, formatter);
	}
}
