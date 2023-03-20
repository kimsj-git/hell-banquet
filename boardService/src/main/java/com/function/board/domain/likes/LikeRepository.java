package com.function.board.domain.likes;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeRepository extends MongoRepository<Like, ObjectId> {

	Optional<Like> findByBoardId(@Param("boardId") Long boardId);

	@Query("sele")
	Optional<Like> findByBoardIdAndUserId(Long boardId, String userId);
}
