package goormton.backend.somgil.domain.reservation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {

    private String packageId;
    private String userName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private List<String> selectedOptions;
    private int adultCount;
    private int childCount;
    private int infantCount;
}