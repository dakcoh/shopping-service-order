package gateway.config;

import gateway.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
@Component
@Slf4j
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {
    @Value("${jwt.audience}")
    private Object audience;

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // 요청 헤더에서 토큰 추출
            String token = extractToken(exchange.getRequest().getHeaders());
            if (token == null || !isTokenValid(token)) {
                log.warn("유효하지 않은 JWT 토큰");
                return onError(exchange);
            }
            log.info("JWT 토큰 검증 성공");
            // 다음 필터로 진행
            return chain.filter(exchange);
        };
    }

    private String extractToken(HttpHeaders headers) {
        String bearerToken = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private boolean isTokenValid(String token) {
        try {
            return JWTUtil.validateToken(token, audience);
        } catch (Exception e) {
            log.error("토큰 검증 오류: {}", e.getMessage());
            return false;
        }
    }

    private Mono<Void> onError(ServerWebExchange exchange) {
        log.error("필터 오류: {}", "유효하지 않은 JWT 토큰");
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    public static class Config {
        // 추가 설정이 필요하면 여기서 정의
    }
}
