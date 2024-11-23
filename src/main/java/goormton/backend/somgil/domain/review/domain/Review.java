package goormton.backend.somgil.domain.review.domain;

import goormton.backend.somgil.domain.packages.domain.PackageDetails;
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
    @JoinColumn(name = "package_id", nullable = false)
    private PackageDetails packageDetails; // 리뷰 대상 패키지

    private double rating; // 별점
    private String content; // 리뷰 내용

    private LocalDateTime createdAt; // 작성 시간
    private LocalDateTime updatedAt;

    @Builder
    public Review(User user, PackageDetails packageDetails, double rating, String content) {
        this.user = user;
        this.packageDetails = packageDetails;
        this.rating = rating;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }
}
