package com.hellsfood.dto.board;

import java.time.LocalDateTime;
import java.util.List;

import com.hellsfood.domain.board.Board;
import com.hellsfood.dto.comment.CommentListResponseDto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardResponseDto {

	private final Long id;
	private final String content;
	private final String writer;
	private final LocalDateTime createdAt;
	private final List<CommentListResponseDto> comments;

	@Builder
	public BoardResponseDto(Board entity, List<CommentListResponseDto> dto) {
		this.id = entity.getId();
		this.content = entity.getContent();
		this.writer = entity.getWriter();
		this.createdAt = entity.getCreatedAt();
		this.comments = dto;
	}

}
