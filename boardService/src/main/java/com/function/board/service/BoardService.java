package com.function.board.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.function.board.domain.board.Board;
import com.function.board.domain.board.BoardRepository;
import com.function.board.domain.comment.Comment;
import com.function.board.domain.comment.CommentRepository;
import com.function.board.domain.rating.Rating;
import com.function.board.domain.rating.RatingRepository;
import com.function.board.dto.board.BoardListResponseDto;
import com.function.board.dto.board.BoardResponseDto;
import com.function.board.dto.board.BoardSaveRequestDto;
import com.function.board.dto.board.BoardUpdateRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;
	private final CommentRepository commentRepository;
	private final RatingRepository ratingRepository;

	@Transactional
	public Long save(BoardSaveRequestDto requestDto) {
		return boardRepository.save(requestDto.toEntity()).getId();
	}

	@Transactional(readOnly = true)
	public List<Board> findAll() {
		return boardRepository.findAll();
	}

	@Transactional(readOnly = true)
	public BoardResponseDto findById(Long boardId) {
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
		List<Comment> comments = commentRepository.findByBoardId(boardId);
		return new BoardResponseDto(board, comments);
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

	@Transactional(readOnly = true)
	public List<BoardListResponseDto> fetchBoardPagesBy(Long lastBoardId, int size, String userId) {
		List<BoardListResponseDto> boardList = new ArrayList<>();
		Page<Board> boards = fetchPages(lastBoardId, size);

		for (Board board : boards) {
			Optional<Rating> optionalRating = ratingRepository.findById(board.getId());

			if (optionalRating.isPresent()) {
				Rating rating = optionalRating.get();
				int likeCount = rating.getLikeCount();
				int dislikeCount = rating.getDislikeCount();
				int isEvaluated = 0;

				if (rating.getUsers().containsKey(userId)) {
					// Boolean isLiked =
					isEvaluated = rating.getUsers().get(userId) ? 1 : 2;
				}
				boardList.add(new BoardListResponseDto(board, likeCount, dislikeCount, isEvaluated));
			} else {
				boardList.add(new BoardListResponseDto(board, 0, 0, 0));
			}
		}
		return boardList;
	}

	public Page<Board> fetchPages(Long lastBoardId, int size) {
		PageRequest pageable = PageRequest.of(0, size);
		return boardRepository.findByIdLessThanOrderByCreatedAtDesc(lastBoardId, pageable);
	}

	@Transactional(readOnly = true)
	public Long fetchLatestBoardId() {
		Optional<Board> latestBoard = boardRepository.findTopByOrderByIdDesc();
		return latestBoard.map(Board::getId).orElse(null);
	}

}