package goormton.backend.somgil.domain.packages.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PackageRequest {
    @NotNull
    private String packageId; // 사용자가 선택한 패키지 ID

    @Builder.Default
    private int adultNumber = 0;
    @Builder.Default
    private int childNumber = 0;
    @Builder.Default
    private int orphanNumber = 0;

    private String option;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startDate;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate endDate;
}
