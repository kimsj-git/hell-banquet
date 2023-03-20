package com.hellsfood.apigateway.users.data;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUserId(String userId);

	List<Role> getRolesByUserId(String userId);

	@Query("SELECT u.userId from User u where u.email=:email and u.delFlag is null")
	Optional<String> findActiveUserIdByEmail(String email);

	@Query("SELECT MAX(u.id) from User u")
	Long getLastUID();
}
