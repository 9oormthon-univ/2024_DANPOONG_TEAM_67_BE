    package goormton.backend.somgil.domain.course.domain;

    import com.fasterxml.jackson.annotation.JsonIgnore;
    import goormton.backend.somgil.domain.driver.domain.Driver;
    import goormton.backend.somgil.domain.packages.domain.UserPackage;
    import goormton.backend.somgil.domain.user.domain.User;
    import jakarta.persistence.*;
    import lombok.Builder;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    import java.time.LocalDate;
    import java.time.LocalDateTime;

    @Entity
    @Getter
    @Setter
    @NoArgsConstructor
    public class UserCourse {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "base_course_id")
        @JsonIgnore // 무한 루프 방지
        private BaseCourse baseCourse; // 양방향 관계

//        private Long baseCourseId; // BaseCourse ID 참조
        private Long userPackageId; // UserPackage ID 참조

        private LocalDate date;

        private LocalDateTime startDate;
        private LocalDateTime endDate;

        @ManyToOne(fetch = FetchType.LAZY) // Driver와 양방향 관계 설정
        @JoinColumn(name = "driver_id", insertable = false, updatable = false) // DB 매핑용
        private Driver driver;

//        private Long driverId; // Driver ID 참조 (운전자 배정 시)

        @Builder
        public UserCourse(LocalDate date, Long userPackageId) {
            this.date = date;
            this.userPackageId = userPackageId;
        }

    }
