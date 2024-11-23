package goormton.backend.somgil.domain.review.domain.repository;

import goormton.backend.somgil.domain.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewQueryDslRepository {
}
