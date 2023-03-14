package com.hellsfood.api.check.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hellsfood.api.users.data.UserRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@Api(tags = "중복 체크 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/check")
public class CheckController {
	private final UserRepository userRepository;

	@GetMapping("/id/{id}")
	@ApiOperation(value = "ID 중복 체크", notes = "입력받은 회원정보를 바탕으로 회원을 DB에 등록한다.")
	public ResponseEntity canUseId(
		@PathVariable @ApiParam(value = "중복검사 할 ID", required = true) String id) {
		return ResponseEntity.ok(!id.startsWith("guser") && !userRepository.existsByUserId(id));
	}

	@GetMapping("/email/{email}")
	@ApiOperation(value = "이메일 중복 체크", notes = "입력받은 회원정보를 바탕으로 회원을 DB에 등록한다.")
	public ResponseEntity canUseEmail(
		@PathVariable @ApiParam(value = "중복검사 할 이메일", required = true) String email) {
		return ResponseEntity.ok(!userRepository.existsByEmail(email));
	}

	@GetMapping("/name/{name}")
	@ApiOperation(value = "닉네임 중복 체크", notes = "입력받은 회원정보를 바탕으로 회원을 DB에 등록한다.")
	public ResponseEntity canUseName(
		@PathVariable @ApiParam(value = "중복검사 할 닉네임", required = true) String name) {
		return ResponseEntity.ok(!name.startsWith("guser") && !userRepository.existsByName(name));
	}

}
