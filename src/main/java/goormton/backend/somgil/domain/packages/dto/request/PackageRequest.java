package goormton.backend.somgil.domain.packages.dto.request;

import goormton.backend.somgil.domain.course.dto.CourseRequest;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PackageRequest {
    @NotEmpty
    private String name;

    private String description;
    private boolean isRecommended;

    @NotEmpty
    private List<CourseRequest> courses;

    private List<String> tags;
}
