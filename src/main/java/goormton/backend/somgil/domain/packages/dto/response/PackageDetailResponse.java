package goormton.backend.somgil.domain.packages.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.ArrayList;
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
    private int price;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
    private LocalTime startTime;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
    private LocalTime endTime;
    private List<BaseCourseResponse> courses = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
    private List<DriveCourseResponse> driveCourseResponses = new ArrayList<>();

    @Builder
    public PackageDetailResponse(Long id, String packageId, String name, int price, String description, boolean isRecommended, LocalTime startTime, LocalTime endTime, List<BaseCourseResponse> courses, List<String> tags, List<DriveCourseResponse> driveCourseResponses) {
        this.id = id;
        this.packageId = packageId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.isRecommended = isRecommended;
        this.startTime = startTime;
        this.endTime = endTime;
        if (courses != null) {
            this.courses.addAll(courses);
        }
        if (tags != null) {
            this.tags.addAll(tags);
        }
        if (driveCourseResponses != null) {
            this.driveCourseResponses.addAll(driveCourseResponses);
        }
    }
}
