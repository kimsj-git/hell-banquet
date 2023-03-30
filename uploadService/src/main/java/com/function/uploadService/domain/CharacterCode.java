package com.function.uploadService.domain;

import lombok.Getter;

@Getter
public enum CharacterCode {
	GRD_001("잔반이"),
	SEA_001("해반이"),
	SKY_001("공반이");

	private final String value;

	CharacterCode(String value) {
		this.value = value;
	}

	public static CharacterCode fromValue(String value) {
		for (CharacterCode code : values()) {
			if (code.getValue().equals(value)) {
				return code;
			}
		}
		throw new IllegalArgumentException("유효하지 않은 이름: " + value);
	}

}
