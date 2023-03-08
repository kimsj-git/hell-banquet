package com.hellsfood.api.users.service;

import java.time.LocalDateTime;
import java.util.Collections;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hellsfood.api.roles.data.RoleRepository;
import com.hellsfood.api.users.data.User;
import com.hellsfood.api.users.data.UserRepository;
import com.hellsfood.api.users.dto.RegisterRequestDto;
import com.hellsfood.api.users.dto.UpdateRequestDto;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	@Value("${jwt.secret}")
	private String uniqueKey;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	private final PasswordEncoder passwordEncoder;

	@Transactional
	public Long registerUser(RegisterRequestDto requestDto) {
		requestDto.setPassword(passwordEncoder.encode(requestDto.getPassword()));
		User tmpUser = requestDto.toEntity();
		tmpUser.setRoles(Collections.singletonList(
			roleRepository.findByRoleName("User").orElseThrow(() -> new RuntimeException("권한 설정 중 오류가 발생하였습니다."))));
		return userRepository.save(tmpUser).getId();
	}

	@Transactional
	public String updateUser(String userId, UpdateRequestDto requestDto, String accessToken) {
		User user = getActiveUserWithValidationCheck(userId, accessToken);
		if (user == null) {
			return null;
		}
		if (requestDto.getName() != null) {
			user.setName(requestDto.getName());
		}
		if (requestDto.getEmail() != null) {
			user.setEmail(requestDto.getEmail());
		}
		return userId;
	}

	@Transactional
	public String deleteUser(String id, String accessToken) {
		User user = getActiveUserWithValidationCheck(id, accessToken);
		if (user != null) {
			user.setDelFlag(LocalDateTime.now());
			return id;
		} else {
			return null;
		}
	}

	public boolean idCheck(String userId) {
		return userRepository.existsByUserId(userId);
	}

	public boolean emailCheck(String email) {
		return userRepository.existsByEmail(email);
	}

	public boolean nameCheck(String name) {
		return userRepository.existsByName(name);
	}

	@Transactional
	public String updatePassword(String userId, String newPassword, boolean needValidation, String accessToken) {
		if (needValidation) {
			String extractedId = getUserIdFromAccessToken(accessToken);
			System.out.println("[updatePassword@MemberService]  AccessToken에서 추출한 userId: " + userId);
			System.out.println(newPassword);
			if (!userId.equals(extractedId)) {
				return null;
			}
		}
		User user = getActiveUser(userId);
		String encodedPassword = passwordEncoder.encode(newPassword);
		user.setPassword(encodedPassword);
		userRepository.save(user);
		return user.getUserId();
	}

	@Transactional
	public String getTempPassword(String userId) {
		String tempPassword = makeTempPassword();
		return updatePassword(userId, tempPassword, false, null).equals(userId) ? tempPassword : null;
	}

	private User getActiveUserWithValidationCheck(String userId, String accessToken) {
		return getUserIdFromAccessToken(accessToken).equals(userId) ? getActiveUser(userId) : null;
	}

	public User getActiveUser(String userId) {
		User user = userRepository.findByUserId(userId)
			.orElseThrow(() -> new IllegalArgumentException(userId + " 사용자를 찾을 수 없습니다."));
		if (user.getDelFlag() != null) {
			throw new IllegalArgumentException(userId + " 사용자는 탈퇴한 사용자입니다.");
		} else {
			return user;
		}
	}

	public String getUserIdFromAccessToken(String token) {
		String userId = null;
		try {
			userId = Jwts.parser().setSigningKey(uniqueKey).parseClaimsJws(token).getBody().getSubject();
		} catch (Exception e) {
			System.out.println("AccessToken에서 회원 ID 추출 중 오류가 발생했습니다.\n" + e.getMessage());
		}
		return userId;
	}

	public User getUser(String id) {
		return getActiveUser(id);
	}

	private String makeTempPassword() {
		char[] charSet = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
			'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a',
			'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
			'w', 'x', 'y', 'z'};
		StringBuilder tempPassword = new StringBuilder();

		for (int i = 0; i < 8; i++) {
			int idx = (int)(charSet.length * Math.random());
			tempPassword.append(charSet[idx]);
		}
		return tempPassword.toString();
	}

	public boolean existUserByIdAndEmail(String userId, String email) {
		return userRepository.existsByUserIdAndEmail(userId, email);
	}

	public boolean existActiveUserByIdAndEmail(String userId, String email) {
		if (existUserByIdAndEmail(userId, email)) {
			return getActiveUser(userId) != null;
		}
		return false;
	}
}
