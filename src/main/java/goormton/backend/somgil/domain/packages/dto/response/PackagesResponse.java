package goormton.backend.somgil.domain.packages.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackagesResponse {

    private String name;

    private String packageId;

    private double reviewRating;
    private int reviewNumber;

    private String image1;
    private String image2;
    private String image3;
}
