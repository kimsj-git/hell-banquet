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

import com.hellsfood.api.mail.dto.MailDto;
import com.hellsfood.api.mail.service.MailService;
import com.hellsfood.api.users.data.User;
import com.hellsfood.api.users.dto.PasswordChangeRequestDto;
import com.hellsfood.api.users.dto.RegisterRequestDto;
import com.hellsfood.api.users.dto.TempPasswordRequestDto;
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
	private final MailService mailService;

	@PostMapping("/register")
	@ApiOperation(value = "회원 가입", notes = "입력받은 회원정보를 바탕으로 회원을 DB에 등록한다.")
	public ResponseEntity registerUser(
		@RequestBody @ApiParam(value = "회원가입 정보", required = true) RegisterRequestDto requestDto) {
		return ResponseEntity.ok(userService.registerUser(requestDto));
	}

	@PutMapping("/info/{id}")
	@ApiOperation(value = "정보 변경", notes = "갱신된 사용자 정보를 {id}를 PK로 가지는 레코드에 적용한다.")
	public ResponseEntity updateUser(
		@PathVariable @ApiParam(value = "회원정보를 수정할 사용자의 {id}", required = true) String id,
		@RequestBody @ApiParam(value = "수정할 내용이 담긴 데이터 객체", required = true) UpdateRequestDto requestDto,
		HttpServletRequest request) {
		String accessToken = request.getHeader("Authorization").substring(7);
		String updatedUserId = userService.updateUser(id, requestDto, accessToken);
		if (updatedUserId == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(id + " 사용자의 수정 권한이 없는 사용자입니다.");
		}
		return ResponseEntity.ok(updatedUserId);
	}

	@GetMapping("/info/{id}")
	@ApiOperation(value = "정보 조회", notes = "{id}에 해당하는 사용자 정보를 DB에서 가져온다.")
	public ResponseEntity getUser(@PathVariable @ApiParam(value = "회원정보를 조회할 사용자의 {id}", required = true) String id) {
		User user = userService.getUser(id);
		if (user.getDelFlag() != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("탈퇴한 사용자입니다.");
		} else {
			return ResponseEntity.ok(user);
		}
	}

	@PostMapping("/delete/{id}")
	@ApiOperation(value = "회원 탈퇴", notes = "{id}의 사용자 정보에 탈퇴일(del_flag)을 기록한다.")
	public ResponseEntity deleteUser(@PathVariable @ApiParam(value = "탈퇴할 회원 ID", required = true) String id,
		HttpServletRequest request) {
		String accessToken = request.getHeader("Authorization").substring(7);
		String changedId = userService.deleteUser(id, accessToken);
		if (changedId == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없는 사용자입니다.");
		} else {
			return ResponseEntity.ok(changedId);
		}
	}

	@PutMapping("/pw")
	@ApiOperation(value = "비밀번호 변경", notes = "Request Header의 AccessToken으로부터 사용자 ID를 추출하여 해당 사용자의 비밀번호를 변경한다.")
	public ResponseEntity changePassword(
		@RequestBody @ApiParam(value = "새로운 비밀번호", required = true) PasswordChangeRequestDto requestDto,
		HttpServletRequest request) {
		String accessToken = request.getHeader("Authorization").substring(7);
		String updatedId = userService.updatePassword(requestDto.getUserId(), requestDto.getNewPassword(), true,
			accessToken);
		if (updatedId == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("토큰 정보와 요청 정보가 일치하지 않습니다.");
		} else {
			return ResponseEntity.ok(updatedId + "님의 비밀번호가 정상적으로 수정되었습니다.");
		}
	}

	@PostMapping("/pw")
	@ApiOperation(value = "임시 비밀번호 발급", notes = "비밀번호를 재설정하려는 ID와 이메일을 받아 회원 본인인지 확인하고, 맞다면 8자 구성의 임시 비밀번호를 반환한다.")
	public ResponseEntity getTempPassword(
		@RequestBody @ApiParam(value = "임시 비밀번호 발급 요청 정보", required = true) TempPasswordRequestDto requestDto) {
		String userId = requestDto.getUserId();
		String userEmail = requestDto.getEmail();

		boolean isValidInformation = userService.existActiveUserByIdAndEmail(userId, userEmail);

		if (isValidInformation) {
			MailDto mailDto = mailService.createMailAndMakeTempPassword(userId, userEmail);
			mailService.sendMail(mailDto);
			return ResponseEntity.ok("등록된 메일 주소로 임시 비밀번호를 보내드렸습니다.");
		} else {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("입력한 정보에 해당하는 사용자가 없습니다.");
		}
	}
}
