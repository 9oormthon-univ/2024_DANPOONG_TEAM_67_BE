package goormton.backend.somgil.domain.packages.dto.request;

import goormton.backend.somgil.domain.course.dto.request.CourseRequest;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomPackageRequest {

    private String region;
    private int peopleNumber;
    private LocalDate date;
    private LocalDateTime time;
    private String startPlace;
    private String endPlace;
}
