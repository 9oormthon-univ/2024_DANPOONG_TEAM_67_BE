package goormton.backend.somgil.domain.packages.domain;

import goormton.backend.somgil.domain.course.domain.Course;
import goormton.backend.somgil.domain.option.domain.Options;
import goormton.backend.somgil.domain.review.domain.Review;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Packages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String packageId;

    private String name;
    private String description;

    private String type;

    private boolean isRecommended;

    private int AdultPrice;
    private int ChildPrice;
    private int InfantPrice;

//    private double reviewRating;
//    private int reviewNumber;

    private String image1;
    private String image2;
    private String image3;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Course> courseList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    private List<Options> optionList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    private List<Review> reviewList = new ArrayList<>();

    public double computeMeanReviewRating() {
        // Stream API를 사용하여 평균 계산
        return reviewList.stream()
                .mapToDouble(Review::getRating) // 각 리뷰의 rating 가져오기
                .average()                   // 평균 계산
                .orElse(0.0);                // 리뷰가 없을 경우 기본값 0.0
    }

    public int getReviewNumber() {
        return reviewList.size();
    }
}
