package goormton.backend.somgil.domain.driver.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import goormton.backend.somgil.domain.course.domain.UserCourse;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

// 운전기사 정보를 위한 데이터 클래스
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    private String driverId;
    private String name;
    private String contact;

    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore // 무한 루프 방지
    private List<UserCourse> courses = new ArrayList<>();

    @Builder
    public Driver(String driverId, String name, String contact) {
        this.driverId = driverId;
        this.name = name;
        this.contact = contact;
    }

    public void addCourse(UserCourse course) {
        if (course != null) {
            this.courses.add(course);
            course.setDriver(this); // 양방향 관계 설정
        }
    }

    public void removeCourse(UserCourse course) {
        if (course != null) {
            this.courses.remove(course);
        }
    }
}
