package com.function.menu.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ExcelizedMenuRegisterRequestDto {
	private String managerId;
	private String type;
	private String category;
	private String feature;
	private List<String> menuItems;
	private List<String> menuTypes;
	private LocalDate date;
}