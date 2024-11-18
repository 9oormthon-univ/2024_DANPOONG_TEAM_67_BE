package goormton.backend.somgil.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull(message = "Username cannot be blank") String username,
        @NotNull(message = "Password cannot be blank") String password
) {
}
