package com.function.board.domain.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

	Page<Board> findByIdLessThanOrderByIdDesc(Long lastBoardId, Pageable pageable);

	// @Modifying
	// @Query("update Board b set b.likeCount = b.likeCount + 1 where b.id = :id")
	// int updateLike(@Param("id") Long id);
	//
	// @Modifying
	// @Query("update Board b set b.dislikeCount = b.dislikeCount + 1 where b.id = :id")
	// int updateDislike(@Param("id") Long id);

}
