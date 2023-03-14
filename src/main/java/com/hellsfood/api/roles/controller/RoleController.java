package com.hellsfood.api.roles.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hellsfood.api.roles.dto.RoleRegisterRequestDto;
import com.hellsfood.api.roles.service.RoleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@Api(tags = "권한 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/role")
public class RoleController {

	private final RoleService roleService;

	@PostMapping("/register")
	@ApiOperation(value = "권한 등록", notes = "입력받은 문자열을 권한의 이름으로 취급하여 새로운 권한을 생성한다.")
	public ResponseEntity registerUser(
		@RequestBody @ApiParam(value = "새로 생성할 권한의 이름", required = true) RoleRegisterRequestDto requestDto) {
		return ResponseEntity.ok(roleService.registerRole(requestDto.getRoleName()));
	}
}
