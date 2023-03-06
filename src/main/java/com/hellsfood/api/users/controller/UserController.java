package com.hellsfood.api.users.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hellsfood.api.users.data.User;
import com.hellsfood.api.users.dto.RegisterRequestDto;
import com.hellsfood.api.users.dto.UpdateRequestDto;
import com.hellsfood.api.users.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@Api(tags = "회원 관리 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/useri")
public class UserController {
	private final UserService userService;

	@PostMapping("/register")
	@ApiOperation(value = "회원 가입", notes = "입력받은 회원정보를 바탕으로 회원을 DB에 등록한다.")
	public ResponseEntity registerUser(
		@RequestBody @ApiParam(value = "회원가입 정보", required = true) RegisterRequestDto requestDto) {
		return ResponseEntity.ok(userService.registerUser(requestDto));
	}

	@PutMapping("/info/{id}")
	@ApiOperation(value = "정보 변경", notes = "갱신된 사용자 정보를 {id}를 PK로 가지는 레코드에 적용한다.")
	public ResponseEntity updateMember(
		@PathVariable @ApiParam(value = "회원정보를 수정할 사용자의 {id}", required = true) String id,
		@RequestBody @ApiParam(value = "수정할 내용이 담긴 데이터 객체", required = true) UpdateRequestDto requestDto,
		HttpServletRequest request) {
		String accessToken = request.getHeader("Authorization").substring(7);
		String updatedUserId = userService.updateInfo(id, requestDto, accessToken);
		if (updatedUserId == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(id + " 사용자의 수정 권한이 없는 사용자입니다.");
		}
		return ResponseEntity.ok(updatedUserId);
	}

	@GetMapping("/info/{id}")
	@ApiOperation(value = "정보 조회", notes = "{id}에 해당하는 사용자 정보를 DB에서 가져온다.")
	public ResponseEntity findById(@PathVariable @ApiParam(value = "회원정보를 조회할 사용자의 {id}", required = true) String id) {
		User user = userService.findByUserId(id);
		if (user.getDelFlag() != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("탈퇴한 사용자입니다.");
		} else {
			return ResponseEntity.ok(user);
		}
	}

	@PostMapping("/delete/{id}")
	@ApiOperation(value = "회원 탈퇴", notes = "{id}의 사용자 정보에 탈퇴일(del_flag)을 기록한다.")
	public ResponseEntity deleteMember(@PathVariable @ApiParam(value = "탈퇴할 회원 ID", required = true) String id,
		HttpServletRequest request) {
		String accessToken = request.getHeader("Authorization").substring(7);
		String changedId = userService.deleteUser(id, accessToken);
		if (changedId == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없는 사용자입니다.");
		} else {
			return ResponseEntity.ok(changedId);
		}
	}
}
