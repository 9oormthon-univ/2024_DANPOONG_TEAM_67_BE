package goormton.backend.somgil.domain.review.domain;

import goormton.backend.somgil.domain.packages.domain.Packages;
import goormton.backend.somgil.domain.reservation.domain.Reservation;
import goormton.backend.somgil.domain.user.domain.User;
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
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 리뷰 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "packages_id", nullable = false)
    private Packages packages;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    private double rating; // 별점
    private String content; // 리뷰 내용

    private LocalDateTime createdAt; // 작성 시간
    private LocalDateTime updatedAt;

    @Builder
    public Review(User user, Packages packages, Reservation reservation, double rating, String content) {
        this.user = user;
        this.packages = packages;
        this.reservation = reservation;
        this.rating = rating;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }
}
