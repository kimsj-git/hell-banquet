package com.function.board.dto.board;

import java.time.LocalDateTime;

import com.function.board.domain.board.Board;

import lombok.Getter;

@Getter
public class BoardListResponseDto {

	private final Long id;
	private final String content;
	private final String writer;
	private final LocalDateTime createdAt;
	private final int commentCount;
	private final int likeCount;
	private final int dislikeCount;
	private final int isEvaluated;    // 0: 평가 X , 1: 좋아요 , 2: 싫어요
	//좋아요 개수 + 싫어요 개수 + 유저가 해당 게시글을 평가했는지 여부를 같이 넣어서 전달하기

	public BoardListResponseDto(Board entity, int likeCount, int dislikeCount, int isEvaluated) {
		this.id = entity.getId();
		this.content = entity.getContent();
		this.writer = entity.getWriter();
		this.createdAt = entity.getCreatedAt();
		this.commentCount = entity.getComments().size();
		this.likeCount = likeCount;
		this.dislikeCount = dislikeCount;
		this.isEvaluated = isEvaluated;
	}

}
