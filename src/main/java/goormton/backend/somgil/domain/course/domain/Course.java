package goormton.backend.somgil.domain.course.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
