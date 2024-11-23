package goormton.backend.somgil.domain.review.presentation;

import goormton.backend.somgil.domain.reservation.dto.response.ReservationResponse;
import goormton.backend.somgil.domain.review.dto.request.DeleteReviewRequest;
import goormton.backend.somgil.domain.review.dto.request.UpdateReviewRequest;
import goormton.backend.somgil.domain.review.dto.request.WriteReviewRequest;
import goormton.backend.somgil.domain.review.dto.response.ReviewResponse;
import goormton.backend.somgil.domain.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "로그인한 유저의 리뷰 리스트를 반환", description = "패키지 정보를 같이 반환.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리스트 반환 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ReviewResponse.class))}),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = {@Content(mediaType = "application/json")}),
    })
    @GetMapping("/list/me")
    public ResponseEntity<List<ReviewResponse>> getMyReviews() {
        List<ReviewResponse> reviewResponseList = reviewService.getReviewsByUser();
        return ResponseEntity.ok(reviewResponseList);
    }

    @Operation(summary = "패키지 리뷰 작성", description = "패키지에 대한 내용을 리뷰로 작성.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 작성 성공", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = {@Content(mediaType = "application/json")}),
    })
    @PostMapping("/package/write")
    public ResponseEntity<?> writeReviews(@RequestBody WriteReviewRequest writeReviewRequest) {
        try {
            reviewService.writeReview(writeReviewRequest);
            return ResponseEntity.status(HttpStatus.OK).body("리뷰 작성 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("리뷰 작성 중 서버 에러 발생: " + e.getMessage());
        }
    }

    @Operation(summary = "리뷰 수정", description = "작성한 리뷰를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 수정 성공", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = {@Content(mediaType = "application/json")}),
    })
    @PutMapping("/package/update")
    public ResponseEntity<?> updateReview(@RequestBody UpdateReviewRequest updateReviewRequest) {
        try {
            reviewService.updateReview(updateReviewRequest); // 리뷰 수정 로직 호출
            return ResponseEntity.ok().body("리뷰가 성공적으로 수정되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리뷰 수정 중 문제가 발생했습니다.");
        }
    }

    @Operation(summary = "리뷰할 패키지의 예약 내용", description = "예약에 대한 세부 내용을 .")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내용 반환 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponse.class))}),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = {@Content(mediaType = "application/json")}),
    })
    @GetMapping("/package/write")
    public ResponseEntity<ReservationResponse> getReservationData(@RequestParam String packageId) {
        return ResponseEntity.ok(reviewService.getReservation(packageId));
    }

    @Operation(summary = "패키지 리뷰 삭제", description = "예약에 대한 세부 내용을 .")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 삭제 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponse.class))}),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = {@Content(mediaType = "application/json")}),
    })
    @DeleteMapping("/package")
    public ResponseEntity<?> deleteReview(@RequestBody DeleteReviewRequest deleteReviewRequest) {
        try {
            reviewService.deleteReview(deleteReviewRequest); // 리뷰 삭제 로직 호출
            return ResponseEntity.ok().body("리뷰가 성공적으로 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("리뷰를 찾을 수 없습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("리뷰 삭제 중 문제가 발생했습니다.");
        }
    }
}
