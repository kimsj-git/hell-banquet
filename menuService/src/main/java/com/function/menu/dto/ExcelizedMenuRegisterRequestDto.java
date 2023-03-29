package com.function.menu.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExcelizedMenuRegisterRequestDto {

	private int requestNo;
	private String managerId;
	private String type;
	private String category;
	private String feature;
	private List<String> menuItems;
	private List<String> menuTypes;
	private String date;

	public ExcelizedMenuRegisterRequestDto(int requestNo) {
		this.requestNo = requestNo;
	}
}
