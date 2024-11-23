//package goormton.backend.somgil.domain.review.presentation;
//
//import goormton.backend.somgil.domain.packages.dto.response.PackageDetailResponse;
//import goormton.backend.somgil.domain.review.domain.Review;
//import goormton.backend.somgil.domain.review.dto.response.ReviewResponse;
//import goormton.backend.somgil.domain.review.service.ReviewService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/review")
//@RequiredArgsConstructor
//public class ReviewController {
//
//    private final ReviewService reviewService;
//
//    @Operation(summary = "로그인한 유저의 리뷰 리스트를 반환", description = "패키지 정보를 같이 반환.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "리스트 반환 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ReviewResponse.class))}),
//            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = {@Content(mediaType = "application/json")}),
//    })
//    @GetMapping("/list/me")
//    public ResponseEntity<ReviewResponse> getMyReviews() {
//        ReviewResponse reviewResponse = reviewService.getReviewsByUser();
//        return ResponseEntity.ok()
//    }
//}
