package com.hellsfood.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hellsfood.domain.board.Board;
import com.hellsfood.domain.board.BoardRepository;
import com.hellsfood.domain.comment.Comment;
import com.hellsfood.domain.comment.CommentRepository;
import com.hellsfood.domain.rating.Rating;
import com.hellsfood.domain.rating.RatingRepository;
import com.hellsfood.dto.board.BoardListResponseDto;
import com.hellsfood.dto.board.BoardResponseDto;
import com.hellsfood.dto.board.BoardSaveRequestDto;
import com.hellsfood.dto.board.BoardUpdateRequestDto;
import com.hellsfood.dto.comment.CommentListResponseDto;

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
	public BoardResponseDto getBoardById(Long boardId, Long lastCommentId, int size) {
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));

		PageRequest pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Comment> comments = commentRepository.findByBoardIdAndIdLessThanOrderByCreatedAtDesc(boardId,
			lastCommentId, pageable);

		List<CommentListResponseDto> commentDtos = comments.stream()
			.map(CommentListResponseDto::new)
			.collect(Collectors.toList());

		return new BoardResponseDto(board, commentDtos);
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

	public Board getBoardWithMostLikes(String dateStr) {
		LocalDateTime startDateTime = parseDate(dateStr).atStartOfDay();
		LocalDateTime endDateTime = startDateTime.plusDays(1);

		List<Board> boards = boardRepository.findByCreatedAtBetween(startDateTime, endDateTime);
		if (boards.isEmpty()) {
			return null;
		}

		Board boardWithMostLikes = null;
		int maxLikes = 0;

		for (Board board : boards) {
			Optional<Rating> optionalRating = ratingRepository.findById(board.getId());

			if (optionalRating.isPresent()) {
				Rating rating = optionalRating.get();
				int likeCount = rating.getLikeCount();

				if (maxLikes <= likeCount) {
					boardWithMostLikes = board;
					maxLikes = likeCount;
				}
			}
		}
		return boardWithMostLikes;
	}

	private LocalDate parseDate(String dateStr) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return LocalDate.parse(dateStr, formatter);
	}

}