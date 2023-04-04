package com.hellsfood.api;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hellsfood.dto.JanbaniDto;
import com.hellsfood.dto.JanbaniRequestDto;
import com.hellsfood.dto.JanbaniUpdateRequestDto;
import com.hellsfood.service.ImageService;
import com.hellsfood.service.JanbaniService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/janban")
@RequiredArgsConstructor
public class JanbaniController {

	private final JanbaniService janbaniService;
	private final ImageService imageService;

	@ApiOperation(value = "식단 특성에 따른 잔반이")
	@PutMapping
	public ResponseEntity<JanbaniDto> saveJanbani(@RequestBody JanbaniRequestDto requestDto) {
		JanbaniDto janbaniDto = janbaniService.createJanban(requestDto).toDto();
		return ResponseEntity.status(HttpStatus.CREATED).body(janbaniDto);
	}

	@ApiOperation(value = "소품 획득에 따른 잔반이 변경")
	@PutMapping("/updateJanbani")
	public ResponseEntity<JanbaniDto> updateJanbaniCode(@RequestBody JanbaniUpdateRequestDto requestDto) {
		JanbaniDto janbaniDto = janbaniService.updateJanbanCode(requestDto).toDto();
		return ResponseEntity.status(HttpStatus.CREATED).body(janbaniDto);
	}

	@ApiOperation(value = "today에 잔반이가 있는지 리턴")
	@GetMapping("/hasJanbani")
	public Boolean hasJanbani(
		@RequestParam String userId, @RequestParam String today
	) {
		return janbaniService.hasJanbanInDate(userId, today);
		// return ResponseEntity.status(HttpStatus.CREATED).body(janbani);
	}

	@ApiOperation(value = "고유 코드로 잔반이 이미지 조회")
	@GetMapping
	public ResponseEntity<?> getImage(@RequestParam String janbanCode) {
		Resource resource = imageService.getImage(janbanCode);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		return new ResponseEntity<>(resource, headers, HttpStatus.OK);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

	@ExceptionHandler(IOException.class)
	public ResponseEntity<String> handleIOException(IOException e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image upload failed: " + e.getMessage());
	}

}

