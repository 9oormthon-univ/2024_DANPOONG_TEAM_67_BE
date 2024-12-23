package goormton.backend.somgil.domain.user.dto.response;

import goormton.backend.somgil.global.config.security.AuthTokens;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponse {
    private Long id;
    private String nickname;
    private String email;
    private String token;

    public LoginResponse(Long id, String nickname, String email, String token) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.token = token;
    }
}
