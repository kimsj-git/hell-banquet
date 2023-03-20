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
@Document(collection = "like")
public class Like {

	@Id
	private ObjectId id;

	@Field("board_id")
	@Indexed(unique = true)
	private Long boardId;

	@Field("like_count")
	private int likeCount;

	@Field("like_users")
	private Map<String, Boolean> likeUsers;

	@Builder
	public Like(Long boardId, Map<String, Boolean> likeUsers) {
		this.boardId = boardId;
		this.likeUsers = likeUsers;
		this.likeCount = likeUsers.size();
	}

	public void addLike(String userId) {
		if (this.likeUsers == null) {
			this.likeUsers = new HashMap<>();
		}
		this.likeUsers.put(userId, true);
	}

	public void removeLike(String userId) {
		if (this.likeUsers != null) {
			this.likeUsers.remove(userId);
		}
	}

	public boolean findLike(String userId) {
		return this.likeUsers.containsKey(userId);
	}

	public void updateLikeCount() {
		this.likeCount = this.likeUsers.size();
	}

}
