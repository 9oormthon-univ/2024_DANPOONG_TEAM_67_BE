package goormton.backend.somgil.domain.packages.dto.response;

import goormton.backend.somgil.domain.course.dto.response.CourseResponse;
import goormton.backend.somgil.domain.review.dto.response.ReviewResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackagesDetailResponse {

    private String name;
    private String description;

    private String packageId;

    private String type;

    private boolean isRecommended;

    private int AdultPrice;
    private int ChildPrice;
    private int InfantPrice;

    private double reviewRating;
    private int reviewNumber;

    private String image1;
    private String image2;
    private String image3;

    @Builder.Default
    private List<CourseResponse> courseList = new ArrayList<>();
    @Builder.Default
    private List<ReviewResponse> reviewList = new ArrayList<>();
}
