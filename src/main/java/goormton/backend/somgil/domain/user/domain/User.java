package goormton.backend.somgil.domain.user.domain;

import goormton.backend.somgil.domain.driver.domain.Driver;
import goormton.backend.somgil.domain.packages.domain.Packages;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Packages> packages = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Driver> drivers = new ArrayList<>();

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

    public void addDriver(Driver driver) {
        this.drivers.add(driver);
    }
}
