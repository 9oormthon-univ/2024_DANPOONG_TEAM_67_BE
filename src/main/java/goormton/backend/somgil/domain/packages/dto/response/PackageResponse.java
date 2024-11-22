package goormton.backend.somgil.domain.packages.dto.response;

import goormton.backend.somgil.domain.course.domain.Course;
import goormton.backend.somgil.domain.course.domain.Tag;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PackageResponse {

    private String name;
    private String description;
    private List<Course> courses;
    private List<String> tags = new ArrayList<>();

    @Builder
    public PackageResponse(String name, String description, List<Course> courses, List<String> tags) {
        this.name = name;
        this.description = description;
        this.courses = courses;
        this.tags = tags;
    }
}
