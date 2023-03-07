package com.function.board.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.function.board.domain.board.BoardRepository;
import com.function.board.domain.comment.Comment;
import com.function.board.domain.comment.CommentRepository;
import com.function.board.dto.comment.CommentListResponseDto;
import com.function.board.dto.comment.CommentSaveRequestDto;
import com.function.board.dto.comment.CommentUpdateRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final BoardRepository boardRepository;

	@Transactional
	public Long save(Long boardId, CommentSaveRequestDto requestDto) {
		var board = boardRepository.findById(boardId)
			.orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

		return commentRepository.save(new Comment(board, requestDto)).getId();
	}

	@Transactional(readOnly = true)
	public List<CommentListResponseDto> findAll(Long boardId) {
		return commentRepository.findByBoardId(boardId).stream()
			.map(CommentListResponseDto::new)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public CommentListResponseDto findById(Long commentId) {
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
		return new CommentListResponseDto(comment);
	}

	@Transactional
	public Long update(Long commentId, CommentUpdateRequestDto requestDto) {
		var comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

		comment.update(requestDto);
		return commentId;
	}

	@Transactional
	public void delete(Long commentId) {
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

		commentRepository.delete(comment);
	}
}
