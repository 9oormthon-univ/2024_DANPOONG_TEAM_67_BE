package goormton.backend.somgil.domain.driver.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DriverResponse {

    private String driverId;
    private String name;
    private String contact;

    @Builder
    public DriverResponse(String driverId, String name, String contact) {
        this.driverId = driverId;
        this.name = name;
        this.contact = contact;
    }
}
