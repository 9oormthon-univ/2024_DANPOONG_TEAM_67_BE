package goormton.backend.somgil.domain.user.service;

import goormton.backend.somgil.domain.user.domain.User;
import goormton.backend.somgil.domain.user.domain.repository.UserRepository;
import goormton.backend.somgil.domain.user.dto.response.UserDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDataResponse getUserData() {
        User loggedUser = getCurrentUser();

        UserDataResponse userDataResponse = UserDataResponse.builder()
                .email(loggedUser.getEmail())
                .nickname(loggedUser.getNickname())
                .kakaoId(loggedUser.getKakaoId())
                .build();

        return userDataResponse;
    }

    private User getCurrentUser() {
        // 현재 로그인된 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("현재 인증된 사용자가 없습니다.");
        }

        User currentUser = (User) authentication.getPrincipal();
        // Ensure the user exists in the database
        return userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));
    }
}
