package goormton.backend.somgil.domain.course.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequest {
    @NotEmpty
    private String region;

    @NotEmpty
    private String place;

    private String description;
    private String image;

    @NotNull
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;
}
