package com.function.board.domain.evaluation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.function.board.domain.board.Board;
import com.function.board.dto.RatingType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "evaluation")
public class Evaluation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "evaluation_no")
	private Long id;

	@Column(name = "rating")
	@Enumerated(EnumType.STRING)
	private RatingType type;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id", nullable = false)
	private Board board;
	//
	// @Column(name = "user_id", nullable = false)
	// private Long userId;

	// @Builder
	// public Like(Board board, RatingType type, Long userId) {
	// 	this.board = board;
	// 	this.userId = userId;
	// 	this.type = type;
	// }

	public Evaluation(Board board, RatingType type) {
		this.board = board;
		this.type = type;
	}

	public void modifyType(RatingType type) {
		this.type = type;
	}

}
