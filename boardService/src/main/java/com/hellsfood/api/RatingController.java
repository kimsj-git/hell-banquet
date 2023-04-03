package com.hellsfood.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hellsfood.domain.rating.Rating;
import com.hellsfood.dto.RatingRequestDto;
import com.hellsfood.service.RatingService;

import lombok.RequiredArgsConstructor;

// @CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/boards/{boardId}")
public class RatingController {

	private final RatingService ratingService;

	@PutMapping("/like")
	public ResponseEntity<Rating> likeToBoard(@PathVariable Long boardId,
		@RequestBody RatingRequestDto dto) {
		Rating rating = ratingService.reactToBoard(boardId, dto.getUserId(), true);
		return ResponseEntity.ok(rating);
	}

	@PutMapping("/dislike")
	public ResponseEntity<Rating> dislikeToBoard(@PathVariable Long boardId,
		@RequestBody RatingRequestDto dto) {
		Rating rating = ratingService.reactToBoard(boardId, dto.getUserId(), false);
		return ResponseEntity.ok(rating);
	}

	@GetMapping("/like/count")
	public ResponseEntity<Integer> getLikeCount(@PathVariable Long boardId) {
		Rating rating = ratingService.getRating(boardId);
		return ResponseEntity.ok(rating.getLikeCount());
	}

	@GetMapping("/dislike/count")
	public ResponseEntity<Integer> getDislikeCount(@PathVariable Long boardId) {
		Rating rating = ratingService.getRating(boardId);
		return ResponseEntity.ok(rating.getDislikeCount());
	}

}
