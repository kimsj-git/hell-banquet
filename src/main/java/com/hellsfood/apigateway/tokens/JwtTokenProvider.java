package com.hellsfood.apigateway.tokens;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hellsfood.apigateway.tokens.data.RefreshToken;
import com.hellsfood.apigateway.tokens.data.RefreshTokenRepository;
import com.hellsfood.apigateway.tokens.dto.JwtTokenDto;
import com.hellsfood.apigateway.users.data.Role;
import com.hellsfood.apigateway.users.data.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

	@Value("${jwt.secret}")
	private String UNIQUE_KEY;

	private final int ACCESS_TOKEN_VALID_TIME = 1000 * 60 * 60 * 12; // AccessToken 유효시간 : 12시간 -> QA 테스트시 30초

	private final UserRepository userRepository;

	private final RefreshTokenRepository refreshTokenRepository;


	@PostConstruct
	protected void init() {
		UNIQUE_KEY = Base64.getEncoder().encodeToString(UNIQUE_KEY.getBytes());
	}

	public JwtTokenDto resolveToken(ServerHttpRequest request) {
		JwtTokenDto tokenDto = new JwtTokenDto();
		HttpHeaders requestHeader = request.getHeaders();
		tokenDto.setAccessToken(requestHeader.get("Authorization").get(0).substring(7));
		tokenDto.setRefreshToken(requestHeader.get("refreshToken").get(0).substring(7));
		if (!refreshTokenRepository.existsByRefreshToken(tokenDto.getRefreshToken())) {
			tokenDto = null;
		}
		System.out.println("[resolveToken@JwtTokenProvider]" + tokenDto);
		return tokenDto;
	}

	// 유저 정보를 토대로 AccessToken, RefreshToken을 생성하는 메서드
	public String reCreateToken(String userId) {
		Claims claims = Jwts.claims().setSubject(userId);
		claims.put("roles", userRepository.getRolesByUserId(userId));
		Date now = new Date();

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME)) // Access Token의 만료 날짜 설정.
			.signWith(SignatureAlgorithm.HS256, UNIQUE_KEY)
			.compact();
	}

	// Jwt 토큰에서 회원 ID 추출
	public String getUserIdFromAccessToken(String token) {
		return Jwts.parser().setSigningKey(UNIQUE_KEY).parseClaimsJws(token).getBody().getSubject();
	}

	public String getUserIdFromRefreshToken(String refreshToken) {
		Optional<RefreshToken> refreshTokenDto = refreshTokenRepository.findByRefreshToken(refreshToken);

		return refreshTokenDto.map(RefreshToken::getUserId).orElse(null);
	}

	public List<Role> getRolesFromAccessToken(String token) {
		try {
			Jws<Claims> jws = Jwts.parser().setSigningKey(UNIQUE_KEY).parseClaimsJws(token);
			System.out.println(jws.getBody().get("roles").getClass());
			// JWT 토큰에서 권한 추출시 발생하는 Type Casting 문제 (Role이 아니라 LinkedHashMap이 나옴) 해결 방법
			// https://storiaquotidiana.tistory.com/48
			// https://www.baeldung.com/jackson-linkedhashmap-cannot-be-cast -> 3번 항목 방식으로 해결함.
			ObjectMapper mapper=new ObjectMapper();
			List<Role> roles= mapper.convertValue(jws.getBody().get("roles"), new TypeReference<List<Role>>() {});
			return roles;
		} catch (ExpiredJwtException e) {
			return null;
		}
	}

	public boolean isTokenNotExpired(String token) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(UNIQUE_KEY).parseClaimsJws(token);
			return !claims.getBody().getExpiration().before(new Date());
		} catch (SecurityException | MalformedJwtException e) {
			log.info("유효하지 않은 Token !! -> " + token);
		} catch (ExpiredJwtException e) {
			log.info("만료된 Token !! -> " + token);
		} catch (UnsupportedJwtException e) {
			log.info("지원하지 않는 형식의 Token !! -> " + token);
		} catch (IllegalArgumentException e) {
			log.info("Token이 빈 문자열을 반환하였습니다 !! -> " + token);
		}
		return false;
	}

}
