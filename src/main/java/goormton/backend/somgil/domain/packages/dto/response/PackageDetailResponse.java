package goormton.backend.somgil.domain.packages.dto.response;

import goormton.backend.somgil.domain.course.dto.BaseCourseResponse;
import goormton.backend.somgil.domain.course.dto.DriveCourseResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PackageDetailResponse {

    private Long id;
    private String packageId;
    private String name;
    private String description;
    private boolean isRecommended;
    private boolean isCustomized;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<BaseCourseResponse> courses;
    private List<String> tags;
    private List<DriveCourseResponse> driveCourseResponses;

    @Builder
    public PackageDetailResponse(Long id, String packageId, String name, String description, boolean isRecommended, LocalTime startTime, LocalTime endTime, List<BaseCourseResponse> courses, List<String> tags, List<DriveCourseResponse> driveCourseResponses) {
        this.id = id;
        this.packageId = packageId;
        this.name = name;
        this.description = description;
        this.isRecommended = isRecommended;
        this.startTime = startTime;
        this.endTime = endTime;
        this.courses = courses;
        this.tags = tags;
        this.driveCourseResponses = driveCourseResponses;
    }
}
