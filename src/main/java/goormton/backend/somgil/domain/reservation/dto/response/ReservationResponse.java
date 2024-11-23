package goormton.backend.somgil.domain.reservation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.List;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponse {

    private String packageName;
    private String packageId;
    private String userName;
    private String reservationId;

    private String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private String startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private String endDate;

    private List<String> selectedOptions; // 선택된 옵션의 내용
    private int adultCount;
    private int childCount;
    private int infantCount;
    private int totalPrice;

    private String pickupLocation; // 픽업 장소
    private String dropOffLocation; // 도착 장소
}
