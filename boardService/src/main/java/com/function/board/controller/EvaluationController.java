package com.function.board.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.function.board.domain.board.BoardRepository;
import com.function.board.domain.evaluation.Evaluation;
import com.function.board.dto.RatingType;
import com.function.board.service.EvaluationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/board/{board_id}/likes")
@RequiredArgsConstructor
public class EvaluationController {

	private final EvaluationService likeService;
	private final BoardRepository boardRepository;

	@PostMapping("")
	public ResponseEntity<Evaluation> evaluate(
		@PathVariable("board_id") Long boardId,
		@RequestParam(value = "rating", required = true) int rating
		// , Long userId
	) {
		RatingType type;
		if(rating == 1) {
			type = RatingType.LIKE;
		} else {
			type = RatingType.DISLIKE;
		}
		var board = boardRepository.findById(boardId)
			.orElseThrow(() -> new IllegalArgumentException("해당 게시글 존재X"));
		System.out.println(board);
		// Like like = new Like(board, type, userId);
		Evaluation like = new Evaluation(board, type);
		Evaluation savedLike = likeService.saveLikes(like);
		return ResponseEntity.ok(savedLike);
	}

}
