package goormton.backend.somgil.domain.packages.domain;

import goormton.backend.somgil.domain.course.domain.Course;
import goormton.backend.somgil.domain.course.domain.Tag;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Packages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Course> courses;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tag> tags;

    public void addCourses(Course course) {
        courses.add(course);
    }

    @Builder
    public Packages(String name, String description, List<Course> courses, List<Tag> tags) {
        this.name = name;
        this.description = description;
        this.courses = courses;
        this.tags = tags;
    }
}
