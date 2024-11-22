package goormton.backend.somgil.domain.packages.dto.response;

import goormton.backend.somgil.domain.course.dto.CourseResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PackageResponse {

    private String name;
    private String description;
    private boolean isRecommended; // 추천 여부 필드 추가
    private List<CourseResponse> courses;
    private List<String> tags;

    @Builder
    public PackageResponse(String name, String description, boolean isRecommended, List<CourseResponse> courses, List<String> tags) {
        this.name = name;
        this.description = description;
        this.isRecommended = isRecommended;
        this.courses = courses;
        this.tags = tags;
    }
}
