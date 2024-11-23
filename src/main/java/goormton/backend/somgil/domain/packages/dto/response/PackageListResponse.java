package goormton.backend.somgil.domain.packages.dto.response;

import goormton.backend.somgil.domain.course.dto.response.BaseCourseResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PackageListResponse {

    private Long id;
    private String packageId;
    private String name;
    private String description;
    private boolean isRecommended;
    private boolean isCustomized;
    private int price;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
    private LocalTime startTime;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
    private LocalTime endTime;
    private List<BaseCourseResponse> courses;
    private List<String> tags;

    @Builder
    public PackageListResponse(Long id, String packageId, String name, String description, int price, boolean isRecommended, LocalTime startTime, LocalTime endTime, List<BaseCourseResponse> courses, List<String> tags) {
        this.id = id;
        this.packageId = packageId;
        this.name = name;
        this.description = description;
        this.isRecommended = isRecommended;
        this.price = price;
        this.startTime = startTime;
        this.endTime = endTime;
        if (courses != null) {
            this.courses.addAll(courses);
        }
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }
}
