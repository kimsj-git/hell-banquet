package com.hellsfood.dto;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class JanbaniDto {

	private final String userId;
	private final String feature;
	private final String janbanCode;
	private final LocalDateTime updateAt;

	public JanbaniDto(String userId, String feature, String janbanCode, LocalDateTime updateAt) {
		this.userId = userId;
		this.feature = feature;
		this.janbanCode = janbanCode;
		this.updateAt = updateAt;
	}

}
