package com.function.menu.domain;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "menu")
public class Menu {

	@Id
	private long id;

	@ApiModelProperty(example = "영양사 아이디")
	private String managerId;

	@ApiModelProperty(example = "식단 일자")
	private LocalDate date;

	@ApiModelProperty(example = "코스 A/코스 B")
	private String type;

	@ApiModelProperty(example = "식단 카테고리 (한식/일식/중식/양식/일품)")
	private String category;

	@ApiModelProperty(example = "식단 주요 특성 (육/해/공)")
	private String feature;

	@ApiModelProperty(example = "메뉴 이름 리스트")
	private List<String> menuItems;

	@ApiModelProperty(example = "메뉴 타입 리스트")
	private List<String> menuTypes;

	@Builder
	public Menu(long id, String managerId, LocalDate date, String type, String category, String feature,
		List<String> menuItems, List<String> menuTypes) {
		this.id = id;
		this.managerId = managerId;
		this.date = date;
		this.type = type;
		this.category = category;
		this.feature = feature;
		this.menuItems = menuItems;
		this.menuTypes = menuTypes;
	}

}
