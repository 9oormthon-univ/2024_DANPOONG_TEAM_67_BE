package goormton.backend.somgil.domain.user.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoUserResponseDto {
    private String id;
    private String connected_at;
    private KakaoAccount kakao_account;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KakaoAccount {
        private String email;
        private Profile profile;

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Profile {
            private String nickname;
        }
    }
}
