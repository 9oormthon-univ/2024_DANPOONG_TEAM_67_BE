package goormton.backend.somgil.domain.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {

    private String userName;

    private String packageName;

    private double rating; // 별점
    private String content; // 리뷰 내용

    private LocalDateTime createdAt; // 작성 시간
    private LocalDateTime updatedAt;
}
