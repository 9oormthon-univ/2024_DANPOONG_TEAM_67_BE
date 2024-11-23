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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private String startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private String endDate;

    private List<String> selectedOptions; // 선택된 옵션의 내용
    private int adultCount;
    private int childCount;
    private int infantCount;
    private int totalPrice;
}
