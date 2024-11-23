package goormton.backend.somgil.domain.course.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import goormton.backend.somgil.domain.driver.domain.Driver;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BaseCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int day; // 1일차, 2일차 등 일차 구분
    private String region;
    private String place;
    private String description;
    private String image;

    private LocalTime startTime; // 코스 시작 시분초만 저장
    private LocalTime endTime;   // 코스 종료 시분초만 저장

    @OneToMany(mappedBy = "baseCourse", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore // 무한 루프 방지
    private List<UserCourse> userCourses = new ArrayList<>();

    @Builder
    public BaseCourse(int day, String region, String place, String description, String image, LocalTime startTime, LocalTime endTime) {
        this.day = day;
        this.region = region;
        this.place = place;
        this.description = description;
        this.image = image;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
