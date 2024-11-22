package goormton.backend.somgil.domain.packages.domain;

import goormton.backend.somgil.domain.course.domain.Course;
import goormton.backend.somgil.domain.course.domain.Tag;
import goormton.backend.somgil.domain.driver.domain.Driver;
import goormton.backend.somgil.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Column(nullable = false, unique = true)
    private String name;
    private String description;

    private boolean isRecommended;
    private boolean isCustomized;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Course> courses = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tag> tags = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void addCourse(Course course) {
        if (course != null) {
            courses.add(course);
//            course.setPackages(this); // Set the relationship on the course side
            updateStartAndEndTime();
        }
    }

    public void removeCourse(Course course) {
        if (course != null && courses.remove(course)) {
//            course.setPackages(null); // Remove the relationship on the course side
            updateStartAndEndTime();
        }
    }

    @Builder
    public Packages(String name, String description, boolean isRecommended, boolean isCustomized, LocalDateTime startDate, LocalDateTime endDate, List<Course> courses, List<Tag> tags) {
        this.name = name;
        this.description = description;
        this.isRecommended = isRecommended;
        this.isCustomized = isCustomized;
        this.startDate = startDate;
        this.endDate = endDate;
        this.courses = courses;
        this.tags = tags;
    }

    public void updateStartAndEndTime() {
        if (courses != null && !courses.isEmpty()) {
            this.startDate = courses.stream()
                    .map(Course::getStart)
                    .min(LocalDateTime::compareTo)
                    .orElse(null);

            this.endDate = courses.stream()
                    .map(Course::getEnd)
                    .max(LocalDateTime::compareTo)
                    .orElse(null);
        } else {
            this.startDate = null;
            this.endDate = null;
        }
    }

    @PrePersist
    @PreUpdate
    private void prePersistAndUpdate() {
        updateStartAndEndTime();
    }
}
