package goormton.backend.somgil.domain.packages.domain;

import goormton.backend.somgil.domain.course.domain.Tag;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PackageDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String packageId;

    private String name;
    private String description;

    private boolean isRecommended;
    private boolean isCustomized;

    private LocalDate startDate;
    private LocalDate endDate;

    private int price;

    @ElementCollection
    private List<Long> courseIds = new ArrayList<>(); // BaseCourse의 ID 목록 저장

    @ElementCollection
    private List<Long> tagIds = new ArrayList<>();


    @Builder
    public PackageDetails(String packageId, String name, String description, boolean isRecommended, boolean isCustomized, LocalDate startDate, LocalDate endDate) {
        this.packageId = packageId;
        this.name = name;
        this.description = description;
        this.isRecommended = isRecommended;
        this.isCustomized = isCustomized;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void addCourse(Long courseId) {
        if (courseId == null) {
            this.courseIds = new ArrayList<>();
        }
        this.courseIds.add(courseId);
    }

    public void removeCourse(Long courseId) {
        if (courseId != null) {
            this.courseIds.remove(courseId);
        }
    }

    public void addTags(Tag tags) {
        this.tagIds.add(tags.getId());
    }

    public void removeTags(Tag tags) {
        if (tags != null) {
            this.tagIds.remove(tags.getId());
        }
    }
}
