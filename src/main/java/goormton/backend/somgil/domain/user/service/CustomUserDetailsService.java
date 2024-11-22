package goormton.backend.somgil.domain.user.service;

import goormton.backend.somgil.domain.user.domain.CustomUserDetails;
import goormton.backend.somgil.domain.user.domain.User;
import goormton.backend.somgil.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

//    사용자 이름으로 사용자의 정보를 가져오는 메소드
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findById(Long.valueOf(username))
                .orElseThrow(() -> new UsernameNotFoundException("다음 유저의 정보를 찾을 수 없습니다: " + username));
        return new CustomUserDetails(user);
    }
}
