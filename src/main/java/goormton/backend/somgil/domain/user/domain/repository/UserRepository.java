package goormton.backend.somgil.domain.user.domain.repository;

import goormton.backend.somgil.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserQueryDslRepository {
    Optional<User> findByEmail(String email);
    boolean existsByKakaoId(String kakaoId);
    Optional<User> findByKakaoId(String kakaoId);

}
