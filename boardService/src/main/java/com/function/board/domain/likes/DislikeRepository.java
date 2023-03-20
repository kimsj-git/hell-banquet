package com.function.board.domain.likes;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

public interface DislikeRepository extends MongoRepository<Dislike, ObjectId> {

	Optional<Dislike> findByBoardId(@Param("boardId") Long boardId);
}
