package goormton.backend.somgil.domain.packages.dto.response;

import goormton.backend.somgil.domain.course.dto.CourseResponse;
import goormton.backend.somgil.domain.driver.dto.request.DriverResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PackageResponse {

    private Long id;
    private String name;
    private String description;
    private boolean isRecommended;
    private boolean isCustomized;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<CourseResponse> courses;
    private List<String> tags;
    private DriverResponse driver;

    @Builder
    public PackageResponse(Long id, String name, String description, boolean isRecommended, LocalDateTime startDate, LocalDateTime endDate, List<CourseResponse> courses, List<String> tags, DriverResponse driver) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isRecommended = isRecommended;
        this.startDate = startDate;
        this.endDate = endDate;
        this.courses = courses;
        this.tags = tags;
        this.driver = driver;
    }
}
