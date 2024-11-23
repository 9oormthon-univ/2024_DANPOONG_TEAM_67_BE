package goormton.backend.somgil.domain.reservation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponse {

    private String packageName;
    private String userName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private List<String> selectedOptions; // 선택된 옵션의 내용
    private int adultCount;
    private int childCount;
    private int infantCount;
    private int totalPrice;
}
