package goormton.backend.somgil.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AddUserRequest(
        @NotNull(message = "Username cannot be blank") String username,
        @NotNull(message = "Password cannot be blank") String password
) {
}
