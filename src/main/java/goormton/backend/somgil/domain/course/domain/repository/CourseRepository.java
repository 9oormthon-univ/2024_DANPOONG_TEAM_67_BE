package goormton.backend.somgil.domain.course.domain.repository;

import goormton.backend.somgil.domain.course.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
