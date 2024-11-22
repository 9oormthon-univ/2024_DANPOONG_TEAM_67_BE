package goormton.backend.somgil.domain.course.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String region;
    private String place;
    private String description;
    private String image;

//    @OneToMany
//    private List<Tag> tags = new ArrayList<>();
    private LocalDateTime start;
    private LocalDateTime end;

    @Builder
    public Course(String region, String place, String description, String image, LocalDateTime start, LocalDateTime end) {
        this.region = region;
        this.place = place;
        this.description = description;
        this.image = image;
        this.start = start;
        this.end = end;
    }
}
