package goormton.backend.somgil.domain.packages.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomPackageRequest {

    @NotNull
    private String region;

    @Builder.Default
    private List<LocalDate> selectedDates = new ArrayList<>(); // 사용자가 선택한 날짜 리스트
//    private boolean isCustomized;

    public void addSelectedDate(LocalDate selectedDate) {
        this.selectedDates.add(selectedDate);
    }

    public void removeSelectedDate(LocalDate selectedDate) {
        this.selectedDates.remove(selectedDate);
    }
}
