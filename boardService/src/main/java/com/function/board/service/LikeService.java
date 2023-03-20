package com.function.board.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.function.board.domain.likes.Like;
import com.function.board.domain.likes.LikeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService {

	private final LikeRepository likeRepository;

	public boolean findLike(Long boardId, String userId) {
		Optional<Like> optionalLike = likeRepository.findByBoardId(boardId);

		if (optionalLike.isPresent()) {
			Like like = optionalLike.get();
			return like.findLike(userId);
			// return false;
		} else {
			return false;
		}
	}

	public void addLike(Long boardId, String userId) {
		Optional<Like> optionalLike = likeRepository.findByBoardId(boardId);

		if (optionalLike.isPresent()) {
			Like like = optionalLike.get();
			like.addLike(userId);
			like.updateLikeCount();
			likeRepository.save(like);
		} else {
			Map<String, Boolean> likeUsers = new HashMap<>();
			likeUsers.put(userId, true);
			Like like = Like.builder()
				.boardId(boardId)
				.likeUsers(likeUsers)
				.build();
			likeRepository.save(like);
		}
	}

	public void removeLike(Long boardId, String userId) {
		Optional<Like> optionalLike = likeRepository.findByBoardId(boardId);
		if (optionalLike.isPresent()) {
			Like like = optionalLike.get();
			like.removeLike(userId);
			like.updateLikeCount();
			likeRepository.save(like);
		}
	}


	// public void addLike(String userId, Long boardId) {
	// 	Optional<Like> optionalLike = likeRepository.findByBoardId(boardId);
	//
	// 	Like like = optionalLike.orElseGet(Like::new);
	// 	// like.setBoardId(boardId);
	// 	like.addLike(userId);
	// 	likeRepository.save(like);
	// }
	//
	// public void removeLike(String userId, Long boardId) {
	// 	Optional<Like> optionalLike = likeRepository.findByBoardId(boardId);
	//
	// 	if (optionalLike.isPresent()) {
	// 		Like like = optionalLike.get();
	// 		like.removeLike(userId);
	//
	// 		if (like.getLikeUsers().isEmpty()) {
	// 			likeRepository.delete(like);
	// 		} else {
	// 			likeRepository.save(like);
	// 		}
	// 	}
	// }
	//
	public int getLikeCount(Long boardId) {
		Optional<Like> optionalLike = likeRepository.findByBoardId(boardId);
		return optionalLike.map(Like::getLikeCount).orElse(0);
	}

}
