package gateway.service;

import gateway.util.JWTUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import gateway.repository.UserRepository;
import gateway.entity.User;  // User 엔티티를 불러옵니다.

import java.util.ArrayList;

@Service
public class AuthService {

    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME;

    @Autowired
    private UserRepository userRepository;  // DB에서 사용자 정보를 조회

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 로그인 처리: 사용자명과 비밀번호로 인증 후 JWT 토큰 생성
    public String login(String username, String password) {
        // 사용자 정보를 데이터베이스에서 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // User 엔티티를 UserDetails로 변환
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>());  // 권한 목록은 예시로 빈 목록

        // 비밀번호 검증
        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            // 사용자 인증 성공 -> JWT 토큰 생성 (expiration 사용)
            return JWTUtil.generateToken(username, userDetails.getUsername(), EXPIRATION_TIME);
        }

        // 인증 실패 시 null 반환
        return null;
    }
}
