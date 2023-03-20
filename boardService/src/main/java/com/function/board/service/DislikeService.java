package com.function.board.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.function.board.domain.likes.Dislike;
import com.function.board.domain.likes.DislikeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DislikeService {

	private final DislikeRepository dislikeRepository;
	private final LikeService likeService;

	public void addDislike(Long boardId, String userId) {
		if (!likeService.findLike(boardId, userId)) {
			Optional<Dislike> optionalDislike = dislikeRepository.findByBoardId(boardId);

			if (optionalDislike.isPresent()) {
				Dislike dislike = optionalDislike.get();
				dislike.addDislike(userId);
				dislike.updateDislikeCount();
				dislikeRepository.save(dislike);
			} else {
				Map<String, Boolean> dislikeUsers = new HashMap<>();
				dislikeUsers.put(userId, true);
				Dislike dislike = Dislike.builder()
					.boardId(boardId)
					.dislikeUsers(dislikeUsers)
					.build();
				dislikeRepository.save(dislike);
			}
		}
	}

	public void removeDislike(Long boardId, String userId) {
		Optional<Dislike> optionalDislike = dislikeRepository.findByBoardId(boardId);
		if (optionalDislike.isPresent()) {
			Dislike dislike = optionalDislike.get();
			dislike.removeDislike(userId);
			dislike.updateDislikeCount();
			dislikeRepository.save(dislike);
		}
	}


	public int getDislikeCount(Long boardId) {
		Optional<Dislike> optionalLike = dislikeRepository.findByBoardId(boardId);
		return optionalLike.map(Dislike::getDislikeCount).orElse(0);
	}

}
