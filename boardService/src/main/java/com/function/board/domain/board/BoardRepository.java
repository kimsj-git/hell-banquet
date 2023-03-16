package com.function.board.domain.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

	Page<Board> findByIdLessThanOrderByCreatedAtDesc(Long lastBoardId, Pageable pageable);
	
}
