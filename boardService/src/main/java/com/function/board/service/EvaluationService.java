package com.function.board.service;

import org.springframework.stereotype.Service;

import com.function.board.domain.board.BoardRepository;
import com.function.board.domain.evaluation.Evaluation;
import com.function.board.domain.evaluation.EvaluationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EvaluationService {

	private final BoardRepository boardRepository;
	private final EvaluationRepository evaluationRepository;

	public Evaluation saveLikes(Evaluation like) {
		var board = boardRepository.findById(like.getBoard().getId())
			.orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

		Evaluation likes = evaluationRepository.findFirstByBoardId(board.getId());

		//이미 평가한 내역이 있으면
		if(likes != null) {
			likes.modifyType(like.getType());
			return evaluationRepository.save(likes);
		}
		//없으면
		return evaluationRepository.save(like);
	}

}
