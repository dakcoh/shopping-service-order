package gateway.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;

public class JWTUtil {
    @Value("${jwt.secret_key}")
    private static String SECRET_KEY;

    private static final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // 토큰 생성
    public static String generateToken(String username, String userId, long expiration) {
        return Jwts.builder()
                .header().type("JWT").and()
                .issuer("server")
                .subject("subject")
                .audience().add(username).and()
                .claim("iat", System.currentTimeMillis()) // 발행 시간
                .claim("exp", expiration) // 만료 시간
                .claim("userId", userId)
                .signWith(key) // 키로 서명
                .compact();
    }

    // 토큰 검증
    public static boolean validateToken(String token, Object expectedAudience) {
        try {
            // SignedClaims 파싱 및 Audience 검증
            return Jwts.parser()
                    .verifyWith(key) // 서명 키 설정
                    .build()
                    .parseSignedClaims(token) // 서명된 JWT 파싱
                    .getPayload()
                    .getAudience()
                    .equals(expectedAudience); // Audience 비교
        } catch (JwtException e) {
            System.err.println("Invalid Token: " + e.getMessage());
            return false; // 유효하지 않은 경우 false 반환
        }
    }
}
