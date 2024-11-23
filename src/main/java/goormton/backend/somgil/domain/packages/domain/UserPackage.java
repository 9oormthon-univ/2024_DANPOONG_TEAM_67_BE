package goormton.backend.somgil.domain.packages.domain;

import goormton.backend.somgil.domain.course.domain.BaseCourse;
import goormton.backend.somgil.domain.course.domain.DriveCourse;
import goormton.backend.somgil.domain.course.domain.UserCourse;
import goormton.backend.somgil.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private Long packageDetailsId; // PackageDetails ID 참조

    @ElementCollection
    private List<Long> courseIds = new ArrayList<>(); // UserCourse ID 목록 저장

    @ElementCollection
    private List<Long> driveCourseIds = new ArrayList<>(); // DriveCourse ID 목록 저장

    private LocalDateTime startDate; // 사용자 선택 시작일
    private LocalDateTime endDate; // 사용자 선택 종료일

    @Builder
    public UserPackage(LocalDateTime startDate, LocalDateTime endDate, Long packageDetailsId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.packageDetailsId = packageDetailsId;
    }

    public void addUserCourse(UserCourse course) {
        this.courseIds.add(course.getId());
        course.setUserPackageId(this.getId());
    }

    public void addDriveCourse(DriveCourse course) {
        this.driveCourseIds.add(course.getId());
        course.setUserPackageId(this.getId());
    }

//    public void updateStartAndEndTime() {
//        List<BaseCourse> courses = packageDetails.getCourses();
//
//        if (courses != null && !courses.isEmpty()) {
//            this.startDate = courses.stream()
//                    .map(BaseCourse::getStart)
//                    .min(LocalDateTime::compareTo)
//                    .orElse(null);
//
//            this.endDate = courses.stream()
//                    .map(BaseCourse::getEnd)
//                    .max(LocalDateTime::compareTo)
//                    .orElse(null);
//        } else {
//            this.startDate = null;
//            this.endDate = null;
//        }
//    }
//
//    @PrePersist
//    @PreUpdate
//    private void prePersistAndUpdate() {
//        updateStartAndEndTime();
//    }
}
