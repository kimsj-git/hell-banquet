package com.function.board.dto.comment;

import com.function.board.domain.comment.Comment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentSaveRequestDto {

	private String content;
	private String writer;

	@Builder
	public CommentSaveRequestDto(String content, String writer) {
		this.content = content;
		this.writer = writer;
	}

	public Comment toEntity() {
		return Comment.builder()
			.content(content)
			.writer(writer)
			.build();
	}
}
