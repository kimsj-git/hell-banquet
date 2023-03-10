package com.function.board.dto.board;

import java.time.LocalDateTime;

import com.function.board.domain.board.Board;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardListResponseDto {

	private final Long id;
	private final String content;
	private final String writer;
	private final LocalDateTime createdAt;

	@Builder
	public BoardListResponseDto(Board entity) {
		this.id = entity.getId();
		this.content = entity.getContent();
		this.writer = entity.getWriter();
		this.createdAt = entity.getCreatedAt();
	}

}
