package com.hellsfood.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionController {
	@ExceptionHandler(AlreadyCompletedException::class)
	fun handleAlreadyCompletedException(e: AlreadyCompletedException): ResponseEntity<String> {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.message)
	}
}
