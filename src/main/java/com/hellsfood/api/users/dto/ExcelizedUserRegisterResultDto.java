package com.hellsfood.api.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcelizedUserRegisterResultDto {
	private String userId;
	private String name;
	private String errorInfo;
}
