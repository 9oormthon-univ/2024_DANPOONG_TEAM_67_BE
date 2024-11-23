package goormton.backend.somgil.domain.user.domain.repository;

import goormton.backend.somgil.domain.user.domain.User;

import java.util.Optional;

public interface UserQueryDslRepository {
    Optional<User> findFirstAvailableDriver();
}
