package com.function.board.domain.board;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.function.board.domain.BaseTimeEntity;
import com.function.board.dto.BoardUpdateRequestDto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "board")
@SQLDelete(sql = "UPDATE second_project.board SET is_deleted = true WHERE board_id=?")
@Where(clause = "is_deleted = false")
public class Board extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "board_id")
	private Long id;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private String writer;

	@Column(name = "like_count")
	private int likeCount;

	@Column(name = "dislike_count")
	private int dislikeCount;

	@Builder
	public Board(String content, String writer) {
		this.content = content;
		this.writer = writer;
		this.likeCount = 0;
		this.dislikeCount = 0;
	}

	public void update(BoardUpdateRequestDto requestDto) {
		this.content = requestDto.getContent();
	}

}
