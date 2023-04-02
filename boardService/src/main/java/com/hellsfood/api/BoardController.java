package com.hellsfood.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hellsfood.domain.board.Board;
import com.hellsfood.dto.board.BoardListResponseDto;
import com.hellsfood.dto.board.BoardResponseDto;
import com.hellsfood.dto.board.BoardSaveRequestDto;
import com.hellsfood.dto.board.BoardUpdateRequestDto;
import com.hellsfood.service.BoardService;
import com.hellsfood.service.CommentService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

// @CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

	private final BoardService boardService;
	private final CommentService commentService;

	@ApiOperation(value = "게시글 생성")
	@PostMapping()
	public ResponseEntity<Long> save(@RequestBody BoardSaveRequestDto requestDto) {
		return new ResponseEntity<>(boardService.save(requestDto), HttpStatus.OK);
	}

	@ApiOperation(value = "게시글 목록 조회")
	@GetMapping("/list")
	public ResponseEntity<List<Board>> list() {
		return ResponseEntity.ok(boardService.findAll());
	}

	@ApiOperation(value = "게시글 페이징")
	@GetMapping()
	public ResponseEntity<List<BoardListResponseDto>> paging(
		@RequestParam Long lastBoardId, @RequestParam int size, @RequestParam String userId) {
		lastBoardId = lastBoardId == -1 ? boardService.fetchLatestBoardId() + 1: lastBoardId;
		List<BoardListResponseDto> boardList = boardService.fetchBoardPagesBy(lastBoardId, size, userId);
		return new ResponseEntity<>(boardList, HttpStatus.OK);
	}

	@ApiOperation(value = "{id}에 해당하는 게시글 조회")
	@GetMapping("/{id}")
	public ResponseEntity<BoardResponseDto> getBoard(
		@PathVariable Long id,
		@RequestParam Long lastCommentId,
		@RequestParam int size
	) {
		lastCommentId = lastCommentId == -1 ? commentService.fetchLatestCommentId() + 1: lastCommentId;
		return new ResponseEntity<>(boardService.getBoardById(id, lastCommentId, size), HttpStatus.OK);
	}

	@ApiOperation(value = "오늘의 게시글 조회")
	@GetMapping("/today")
	public ResponseEntity<?> getMostLikedBoard(@RequestParam("date") String dateStr) {
		Board board = boardService.getBoardWithMostLikes(dateStr);
		if (board == null) {
			return ResponseEntity.ok().body("아직 오늘 작성된 게시글이 없어요");
		}
		return ResponseEntity.ok().body(board);
	}

	@ApiOperation(value = "게시글 수정")
	@PutMapping("/{id}")
	public ResponseEntity<Long> update(@PathVariable Long id, @RequestBody BoardUpdateRequestDto requestDto) {
		return ResponseEntity.ok(boardService.update(id, requestDto));
	}

	@ApiOperation(value = "게시글 삭제")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		boardService.delete(id);
	}

}
