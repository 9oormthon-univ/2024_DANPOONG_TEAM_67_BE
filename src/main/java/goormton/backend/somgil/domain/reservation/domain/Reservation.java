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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 예약자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", nullable = false)
    private Packages packages; // 예약된 패키지

    private LocalDate date; // 예약 날짜

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "reservation_id")
    private List<Options> selectedOptions; // 선택된 옵션들

    private int adultCount; // 성인 인원
    private int childCount; // 아동 인원
    private int infantCount; // 유아 인원

    private int totalPrice; // 총 가격

    @Builder
    public Reservation(User user, Packages packages, LocalDate date, List<Options> selectedOptions, int adultCount, int childCount, int infantCount, int totalPrice) {
        this.user = user;
        this.packages = packages;
        this.date = date;
        this.selectedOptions = selectedOptions;
        this.adultCount = adultCount;
        this.childCount = childCount;
        this.infantCount = infantCount;
        this.totalPrice = totalPrice;
    }
}
