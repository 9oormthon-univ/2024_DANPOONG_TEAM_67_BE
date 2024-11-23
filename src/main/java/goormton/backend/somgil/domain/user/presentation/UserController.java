package goormton.backend.somgil.domain.user.presentation;

import goormton.backend.somgil.domain.user.dto.response.LoginResponse;
import goormton.backend.somgil.domain.user.dto.response.UserDataResponse;
import goormton.backend.somgil.domain.user.service.KakaoService;
import goormton.backend.somgil.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "user",description = "유저 API")
public class UserController {

    private final KakaoService kakaoService;
    private final UserService userService;

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

    @GetMapping("/data")
    @Operation(summary = "회원 정보 반환", description = "회원의 정보를 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "불러오기 성공 ", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserDataResponse.class))}),
            @ApiResponse(responseCode = "400", description = "불러오기 실패", content = {@Content(mediaType = "application/json")}),
    })
    public ResponseEntity<UserDataResponse> data() {
        UserDataResponse response = userService.getUserData();
        return ResponseEntity.ok(response);
    }
}