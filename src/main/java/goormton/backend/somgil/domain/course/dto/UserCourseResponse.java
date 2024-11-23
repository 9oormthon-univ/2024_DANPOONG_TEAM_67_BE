package goormton.backend.somgil.domain.course.dto;

import goormton.backend.somgil.domain.driver.dto.response.DriverResponse;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCourseResponse {

    private BaseCourseResponse baseCourseResponse;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private DriverResponse driverResponse;
}
