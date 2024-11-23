package goormton.backend.somgil.domain.course.dto;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class BaseCourseResponse {

    private String region;
    private String place;
    private String description;
    private String image;
    private LocalTime startTime;
    private LocalTime endTime;

    @Builder
    public BaseCourseResponse(String region, String place, String description, String image, LocalTime startTime, LocalTime endTime) {
        this.region = region;
        this.place = place;
        this.description = description;
        this.image = image;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
