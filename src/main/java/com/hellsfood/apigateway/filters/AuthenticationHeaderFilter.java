package com.hellsfood.apigateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.hellsfood.apigateway.tokens.JwtTokenProvider;
import com.hellsfood.apigateway.tokens.dto.JwtTokenDto;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthenticationHeaderFilter extends AbstractGatewayFilterFactory<AuthenticationHeaderFilter.Config> {

	private final JwtTokenProvider jwtTokenProvider;

	public AuthenticationHeaderFilter(JwtTokenProvider jwtTokenProvider) {
		super(Config.class);
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public GatewayFilter apply(Config config) {

		return ((exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			ServerHttpResponse response = exchange.getResponse();
			ServerWebExchange newExchange = exchange;

			// Step 1. RequestHeader에서 jwt 토큰 추출
			JwtTokenDto tokenDto = jwtTokenProvider.resolveToken(
				request); // 유효하지 않은 세션 (refreshToken이 DB에 있는 값과 다른 값)인지 판단

			// Step 2. 토큰의 유효성 검사
			if (tokenDto != null) { // 유효한 로그인 세션이라면
				String accessToken = tokenDto.getAccessToken();
				String refreshToken = tokenDto.getRefreshToken();

				if (jwtTokenProvider.isTokenNotExpired(refreshToken)) { // 만약 refreshToken의 유효시간이 아직 남았다면
					if (!jwtTokenProvider.isTokenNotExpired(accessToken)) { // 만약 AccessToken의 유효시간이 만료되었다면
						log.info("유효한 로그인 세션이나, AccessToken이 만료되었습니다. 리다이렉팅 전, 토큰을 재발급합니다.");
						// 재발급 후, 컨텍스트에 다시 넣기
						// Refresh Token으로 사용자 ID 가져오기
						String userId = jwtTokenProvider.getUserIdFromRefreshToken(refreshToken);
						// 토큰 발급
						accessToken = jwtTokenProvider.reCreateToken(userId);
						// 헤더에 토큰 정보(AccessToken, refreshToken) 추가
						response.getHeaders().add("Set-Cookie", "a802-at=Bearer-" + accessToken + "; Path=/; HttpOnly");

						ServerHttpRequest newRequest = exchange.getRequest()
							.mutate()
							.header("Authorization", "Bearer-" + accessToken)
							.build();

						newExchange = exchange.mutate().request(newRequest).build();
					} else {
						log.info("Access Token의 유효시간이 아직 남아있습니다.");
					}
				} else {
					log.info("로그인 세션 유효시간이 만료되었습니다. 백엔드 접근 요청을 거부합니다.");
					return handleUnAuthorized(exchange);
				}
				return chain.filter(newExchange);
			} else {
				return handleUnAuthorized(exchange);
			}
		});

	}

	private Mono<Void> handleUnAuthorized(ServerWebExchange exchange) {
		ServerHttpResponse response = exchange.getResponse();

		response.setStatusCode(HttpStatus.UNAUTHORIZED);
		return response.setComplete();
	}

	@Getter
	@Setter
	public static class Config {
	}

}
