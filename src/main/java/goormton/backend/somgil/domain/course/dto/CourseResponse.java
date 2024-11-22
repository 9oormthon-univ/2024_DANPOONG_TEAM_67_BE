package goormton.backend.somgil.domain.course.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponse {

    private String region;
    private String place;
    private String description;
    private String image;
    private LocalDateTime start;
    private LocalDateTime end;

    @Builder
    public CourseResponse(String region, String place, String description, String image, LocalDateTime start, LocalDateTime end) {
        this.region = region;
        this.place = place;
        this.description = description;
        this.image = image;
        this.start = start;
        this.end = end;
    }
}
