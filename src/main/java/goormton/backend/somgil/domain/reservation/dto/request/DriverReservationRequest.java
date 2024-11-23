package goormton.backend.somgil.domain.reservation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DriverReservationRequest {

    private String pickupLocation;
    private String dropOffLocation;

    private int adultCount;
    private int childCount;
    private int infantCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private String date;
    private String time;
}
