package goormton.backend.somgil.domain.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String nickname;
    private String kakaoId;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> role;

    public boolean isDriver() {
        return role.contains("ROLE_DRIVER");
    }

    private boolean available = true; // 기본값은 예약 가능

    @Builder
    public User(String email, String nickname, String kakaoId) {
        this.kakaoId = kakaoId;
        this.email = email;
        this.nickname = nickname;
        this.role = Collections.singletonList("ROLE_USER");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.role.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return id.toString();
    }

//    public void addPackage(UserPackage pkg) {
//        this.aPackages.add(pkg);
//        pkg.setUser(this); // 양방향 연관관계 설정
//    }
}
