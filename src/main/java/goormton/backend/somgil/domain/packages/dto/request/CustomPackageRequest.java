package goormton.backend.somgil.domain.packages.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomPackageRequest {
    private String region;
    private LocalDate startDate;
    private LocalDate endDate;
//    private boolean isCustomized;
}
