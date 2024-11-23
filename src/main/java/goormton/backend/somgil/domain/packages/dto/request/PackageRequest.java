package goormton.backend.somgil.domain.packages.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PackageRequest {
    @NotNull
    private String packageId; // 사용자가 선택한 패키지 ID

    @Builder.Default
    private List<LocalDate> selectedDates = new ArrayList<>(); // 사용자가 선택한 날짜 리스트
//    private String name;

    public void addSelectedDate(LocalDate selectedDate) {
        if (selectedDates == null) {
            selectedDates = new ArrayList<>();
        }
        selectedDates.add(selectedDate);
    }

    public void removeSelectedDate(LocalDate selectedDate) {
        this.selectedDates.remove(selectedDate);
    }
}
