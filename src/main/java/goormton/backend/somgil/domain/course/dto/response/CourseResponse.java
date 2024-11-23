package goormton.backend.somgil.domain.course.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {
    private int day;
    private String content;
}
