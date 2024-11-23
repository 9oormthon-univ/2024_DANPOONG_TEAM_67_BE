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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
