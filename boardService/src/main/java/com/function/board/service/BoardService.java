package com.function.board.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.function.board.domain.board.Board;
import com.function.board.domain.board.BoardRepository;
import com.function.board.dto.board.BoardListResponseDto;
import com.function.board.dto.board.BoardSaveRequestDto;
import com.function.board.dto.board.BoardUpdateRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;

	@Transactional
	public Long save(BoardSaveRequestDto requestDto) {
		return boardRepository.save(requestDto.toEntity()).getId();
	}

	@Transactional(readOnly = true)
	public List<BoardListResponseDto> findAll() {
		return boardRepository.findAll().stream()
			.map(BoardListResponseDto::new)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public BoardListResponseDto findById(Long boardId) {
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
		return new BoardListResponseDto(board);
	}

	@Transactional
	public Long update(Long boardId, BoardUpdateRequestDto requestDto) {
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
		board.update(requestDto);
		return boardId;
	}

	@Transactional
	public void delete(Long boardId) {
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
		boardRepository.delete(board);
	}

	public List<BoardListResponseDto> fetchBoardPagesBy(Long lastBoardId, int size) {
		Page<Board> boards = fetchPages(lastBoardId, size);
		return boards.stream()
			.map(BoardListResponseDto::new)
			.collect(Collectors.toList());
	}

	public Page<Board> fetchPages(Long lastBoardId, int size) {
		PageRequest pageable = PageRequest.of(0, size);
		return boardRepository.findByIdLessThanOrderByIdDesc(lastBoardId, pageable);
	}

}