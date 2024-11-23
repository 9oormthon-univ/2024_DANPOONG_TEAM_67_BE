package goormton.backend.somgil.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDataResponse {

    private String email;
    private String nickname;
    private String kakaoId;
}
