package goormton.backend.somgil.domain.user.presentation;

import goormton.backend.somgil.domain.user.dto.response.LoginResponse;
import goormton.backend.somgil.domain.user.service.KakaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "user",description = "유저 API")
public class UserController {

    private final KakaoService kakaoService;

    @GetMapping("/login/kakao")
    @Operation(summary = "웹 카카오 로그인", description = "웹 프론트 버전 카카오 로그인")
    public ResponseEntity<LoginResponse> kakaoLogin(@RequestParam String code, HttpServletRequest request) {
        try {
            // 현재 도메인 확인

            return ResponseEntity.ok(kakaoService.kakaoLogin(code));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item Not Found");
        }
    }
}