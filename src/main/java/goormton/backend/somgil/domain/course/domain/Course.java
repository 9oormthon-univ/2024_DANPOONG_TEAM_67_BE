package goormton.backend.somgil.domain.course.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
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
}
