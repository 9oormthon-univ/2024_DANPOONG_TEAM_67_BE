package goormton.backend.somgil.domain.reservation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverReservationResponse {

    private String driverName;
    private String packageId;
    private String contact;
    private String pickupLocation;
    private String dropOffLocation;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private String date;
    private String time;

    private int adultCount;
    private int childCount;
    private int infantCount;

    private int totalPrice;
}
