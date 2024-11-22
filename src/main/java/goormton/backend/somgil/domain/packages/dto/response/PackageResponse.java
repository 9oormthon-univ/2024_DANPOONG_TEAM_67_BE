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
    private List<CourseResponse> courses;
    private List<String> tags;

    @Builder
    public PackageResponse(String name, String description, List<CourseResponse> courses, List<String> tags) {
        this.name = name;
        this.description = description;
        this.courses = courses;
        this.tags = tags;
    }
}
