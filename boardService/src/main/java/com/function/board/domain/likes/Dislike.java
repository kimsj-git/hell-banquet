package com.function.board.domain.likes;

import java.util.HashMap;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "dislike")
public class Dislike {

	@Id
	private ObjectId id;

	@Field("board_id")
	@Indexed(unique = true)
	private Long boardId;

	@Field("dislike_count")
	private int dislikeCount;

	@Field("dislike_users")
	private Map<String, Boolean> dislikeUsers;

	@Builder
	public Dislike(Long boardId, Map<String, Boolean> dislikeUsers) {
		this.boardId = boardId;
		this.dislikeUsers = dislikeUsers;
		this.dislikeCount = dislikeUsers.size();
	}

	public void addDislike(String userId) {
		if (this.dislikeUsers == null) {
			this.dislikeUsers = new HashMap<>();
		}
		this.dislikeUsers.put(userId, true);
	}

	public void removeDislike(String userId) {
		if (this.dislikeUsers != null) {
			this.dislikeUsers.remove(userId);
		}
	}

	public void updateDislikeCount() {
		this.dislikeCount = this.dislikeUsers.size();
	}

}
