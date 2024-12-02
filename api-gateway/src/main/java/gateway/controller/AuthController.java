package gateway.controller;

import gateway.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // 로그인 처리
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        // 사용자 인증 후 JWT 토큰 생성
        String token = authService.login(username, password);

        if (token == null) {
            // 로그인 실패: 인증 실패시 처리
            return ResponseEntity.status(401).body("Invalid Credentials");
        }

        // 로그인 성공: JWT 토큰 반환
        return ResponseEntity.ok(token);
    }
}
