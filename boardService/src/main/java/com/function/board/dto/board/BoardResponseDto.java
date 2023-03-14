package com.function.board.dto.board;

import java.time.LocalDateTime;
import java.util.List;

import com.function.board.domain.board.Board;
import com.function.board.domain.comment.Comment;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardResponseDto {

	private final Long id;
	private final String content;
	private final String writer;
	private final LocalDateTime createdAt;
	private final List<Comment> comments;

	@Builder
	public BoardResponseDto(Board entity, List<Comment> comments) {
		this.id = entity.getId();
		this.content = entity.getContent();
		this.writer = entity.getWriter();
		this.createdAt = entity.getCreatedAt();
		this.comments = comments;
	}

}
