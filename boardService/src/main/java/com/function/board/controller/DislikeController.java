package com.function.board.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.function.board.service.DislikeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/boards/{boardId}/dislikes")
@RequiredArgsConstructor
public class DislikeController {

	private final DislikeService dislikeService;

	@PostMapping
	public ResponseEntity<Void> addLike(@PathVariable Long boardId, @RequestBody String userId) {
		dislikeService.addDislike(boardId, userId);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@DeleteMapping
	public ResponseEntity<Void> removeLike(@PathVariable Long boardId, @RequestBody String userId) {
		dislikeService.removeDislike(boardId, userId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/count")
	public ResponseEntity<Map<String, Integer>> getLikeCount(@PathVariable Long boardId) {
		Map<String, Integer> response = new HashMap<>();
		int dislikeCount = dislikeService.getDislikeCount(boardId);
		response.put("dislikeCount", dislikeCount);
		return ResponseEntity.ok(response);
	}
}
