package goormton.backend.somgil.domain.packages.domain;

import goormton.backend.somgil.domain.course.domain.Course;
import goormton.backend.somgil.domain.course.domain.Tag;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @OneToMany
    private List<Course> courses;

    @OneToMany
    private List<Tag> tags = new ArrayList<>();
}
