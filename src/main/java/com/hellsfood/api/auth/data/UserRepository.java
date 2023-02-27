package com.hellsfood.api.auth.data;

import java.util.Optional;

public interface UserRepository {
	Optional<User> findByUserId(String userId);

	boolean existsByUserIdAndEmail(String userId, String email);

}
