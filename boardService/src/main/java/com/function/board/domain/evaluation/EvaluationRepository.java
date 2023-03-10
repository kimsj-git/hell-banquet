package com.function.board.domain.evaluation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.function.board.dto.RatingType;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

	// Optional<Like> findByUserIdAndBoardId(Long userId, Long boardId);

	@Query("select e from Evaluation e where e.board.id = :boardId")
	Evaluation findFirstByBoardId(@Param("boardId") Long boardId);

	Long countByBoardIdAndType(@Param("board_id") Long boardId, @Param("type") RatingType type);

	// List<Like> findByUserId(Long userId);
	// List<Like> findByBoardId(Long boardId);

}