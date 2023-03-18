package com.hellsfood.api.tokens;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hellsfood.api.auth.data.Role;
import com.hellsfood.api.tokens.dto.JwtTokenDto;

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
	private String uniqueKey;

	private final int ACCESS_TOKEN_VALID_TIME = 1000 * 60 * 60 * 12; // AccessToken 유효시간 : 12시간 -> QA 테스트시 30초
	private final int REFRESH_TOKEN_VALID_TIME = 1000 * 60 * 60 * 24 * 14; // RefreshToken 유효시간 : 2주 -> QA 테스트시 5분

	@PostConstruct
	protected void init() {
		uniqueKey = Base64.getEncoder().encodeToString(uniqueKey.getBytes());
	}

	// 유저 정보를 토대로 AccessToken, RefreshToken을 생성하는 메서드

	public JwtTokenDto createToken(String userId, List<Role> roles) {
		Date now = new Date();

		// Access Token 생성
		String accessToken = createAccessToken(userId, roles, now);

		// Refresh Token 생성
		String refreshToken = Jwts.builder()
			.setExpiration(new Date(now.getTime() + REFRESH_TOKEN_VALID_TIME)) // Refresh Token의 만료 날짜 설정.
			.signWith(SignatureAlgorithm.HS256, uniqueKey)
			.compact();

		return JwtTokenDto.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.userId(userId)
			.build();
	}

	public String createAccessToken(String userId, List<Role> roles, Date now) {
		Claims claims = Jwts.claims().setSubject(userId);
		claims.put("roles", roles);

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME)) // Access Token의 만료 날짜 설정.
			.signWith(SignatureAlgorithm.HS256, uniqueKey)
			.compact();
	}

	// Jwt 토큰에서 회원 ID 추출
	public String getUserIdFromAccessToken(String token) {
		return Jwts.parser().setSigningKey(uniqueKey).parseClaimsJws(token).getBody().getSubject();
	}

	public List<Role> getRolesFromAccessToken(String token) {
		try {
			Jws<Claims> jws = Jwts.parser().setSigningKey(uniqueKey).parseClaimsJws(token);
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
			Jws<Claims> claims = Jwts.parser().setSigningKey(uniqueKey).parseClaimsJws(token);
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
