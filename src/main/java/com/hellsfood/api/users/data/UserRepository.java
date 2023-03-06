package com.hellsfood.api.users.data;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByUserId(String userId);

	boolean existsByEmail(String email);

	boolean existsByName(String name);

	boolean existsByUserIdAndEmail(String userId, String email);

	Optional<User> findByUserIdAndEmail(String userId, String email);


}
