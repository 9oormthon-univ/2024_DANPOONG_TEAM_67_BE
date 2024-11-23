package goormton.backend.somgil.domain.reservation.presentation;

import goormton.backend.somgil.domain.reservation.dto.request.DriverReservationRequest;
import goormton.backend.somgil.domain.reservation.dto.request.ReservationRequest;
import goormton.backend.somgil.domain.reservation.dto.response.DriverReservationResponse;
import goormton.backend.somgil.domain.reservation.dto.response.ReservationResponse;
import goormton.backend.somgil.domain.reservation.service.ReservationService;
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
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @Operation(summary = "패키지 예약", description = "패키지 예약 생성 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "불러오기 성공 ", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponse.class))}),
            @ApiResponse(responseCode = "400", description = "불러오기 실패", content = {@Content(mediaType = "application/json")}),
    })
    @PostMapping("/approval")
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody ReservationRequest request) {
        ReservationResponse response = reservationService.createReservation(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "현지인 기사 예약", description = "현지인 기사 예약 생성 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "불러오기 성공 ", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DriverReservationResponse.class))}),
            @ApiResponse(responseCode = "400", description = "불러오기 실패", content = {@Content(mediaType = "application/json")}),
    })
    @PostMapping("/local")
    public ResponseEntity<DriverReservationResponse> createDriverReservation(@RequestBody DriverReservationRequest request) {
        DriverReservationResponse response = reservationService.createDriverReservation(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "유저의 예약 리스트 반환", description = "유저의 현재 예약 패키지를 모두 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "불러오기 성공 ", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponse.class))}),
            @ApiResponse(responseCode = "400", description = "불러오기 실패", content = {@Content(mediaType = "application/json")}),
    })
    @GetMapping("/list/me")
    public ResponseEntity<List<ReservationResponse>> getAllByUser() {
        List<ReservationResponse> responses = reservationService.findAllByUser();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "예약 취소(삭제)", description = "진행 중인 예약을 취소함.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "불러오기 성공 ", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "불러오기 실패", content = {@Content(mediaType = "application/json")}),
    })
    @PutMapping("/cancelled")
    public ResponseEntity<?> cancelledReservation(@RequestParam String reservationId) {
        try {
            reservationService.cancelledReservation(reservationId); // 예약 취소 로직 호출
            return ResponseEntity.ok("예약이 성공적으로 취소되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("예약을 찾을 수 없습니다: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("예약 취소 중 문제가 발생했습니다: " + e.getMessage());
        }
    }
}
