package goormton.backend.somgil.domain.course.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriveCourseResponse {

    private String driverName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
