package goormton.backend.somgil.domain.course.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class BaseCourseResponse {

    private String region;
    private String place;
    private String description;
    private String image;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
    private LocalTime startTime;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
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
