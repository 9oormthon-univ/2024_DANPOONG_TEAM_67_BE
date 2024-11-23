package goormton.backend.somgil.domain.reservation.domain;

import goormton.backend.somgil.domain.option.domain.Options;
import goormton.backend.somgil.domain.packages.domain.Packages;
import goormton.backend.somgil.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    예약자 관련 필드
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 예약자

    private String status = "IN_PROGRESS";  // COMPLETED, FINISHED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id")
    private Packages packages; // 예약된 패키지

    private LocalDate startDate;
    private LocalDate endDate;

    private String time; // 예약 시간(현지인 기사용)

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "reservation_id")
    private List<Options> selectedOptions; // 선택된 옵션들

    private int adultCount; // 성인 인원
    private int childCount; // 아동 인원
    private int infantCount; // 유아 인원

    private int totalPrice; // 총 가격

    @Builder
    public Reservation(User user, String status, Packages packages, LocalDate startDate, LocalDate endDate, List<Options> selectedOptions, int adultCount, int childCount, int infantCount, int totalPrice, User driver, String pickupLocation, String dropOffLocation, String time) {
        this.user = user;
        this.status = status;
        this.packages = packages;
        this.startDate = startDate;
        this.endDate = endDate;
        this.selectedOptions = selectedOptions;
        this.adultCount = adultCount;
        this.childCount = childCount;
        this.infantCount = infantCount;
        this.totalPrice = totalPrice;
        this.driver = driver;
        this.pickupLocation = pickupLocation;
        this.dropOffLocation = dropOffLocation;
        this.time = time;
    }

//    기사 관련 필드
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private User driver; // 기사 (ROLE_DRIVER 사용자)

    @Column(nullable = false)
    private String pickupLocation; // 픽업 장소

    @Column(nullable = false)
    private String dropOffLocation; // 도착 장소
}
