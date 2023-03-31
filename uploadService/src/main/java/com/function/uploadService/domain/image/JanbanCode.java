package com.function.uploadService.domain.image;

import lombok.Getter;

@Getter
public enum JanbanCode {
	GRD_001("잔반이", "GRD_001"),
	SEA_001("해반이", "SEA_001"),
	SKY_001("공반이", "SKY_001"),
	GRD_002("잔반이2", "GRD_002"),
	SEA_002("해반이2", "SEA_002"),
	SKY_002("공반이2", "SKY_002");

	private final String value;
	private final String code;

	JanbanCode(String value, String code) {
		this.value = value;
		this.code = code;
	}

	public static JanbanCode fromValue(String value) {
		for (JanbanCode code : values()) {
			if (code.getValue().equals(value)) {
				return code;
			}
		}
		throw new IllegalArgumentException("유효하지 않은 이름: " + value);
	}

	public static JanbanCode fromCode(String code) {
		for (JanbanCode janbanCode : values()) {
			if (janbanCode.getCode().equals(code)) {
				return janbanCode;
			}
		}
		throw new IllegalArgumentException("유효하지 않은 코드: " + code);
	}

}
