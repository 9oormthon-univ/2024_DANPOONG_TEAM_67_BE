package goormton.backend.somgil.domain.driver.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// 운전기사 정보를 위한 데이터 클래스
@Getter
@Setter
@Entity
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    private String driverId;
    private String name;
    private String contact;
}
