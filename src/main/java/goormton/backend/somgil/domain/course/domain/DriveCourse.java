package goormton.backend.somgil.domain.course.domain;

import goormton.backend.somgil.domain.driver.domain.Driver;
import goormton.backend.somgil.domain.packages.domain.UserPackage;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DriveCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long driverId; // Driver ID 참조
    private Long userPackageId; // UserPackage ID 참조

    private LocalDateTime startTime; // 운전 시작 시간
    private LocalDateTime endTime;   // 운전 종료 시간

    @Builder
    public DriveCourse(Long driverId, Long userPackageId, LocalDateTime startTime, LocalDateTime endTime) {
        this.driverId = driverId;
        this.userPackageId = userPackageId;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
