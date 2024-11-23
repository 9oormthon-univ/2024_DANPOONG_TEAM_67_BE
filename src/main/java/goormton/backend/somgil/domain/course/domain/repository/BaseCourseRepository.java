package goormton.backend.somgil.domain.course.domain.repository;

import goormton.backend.somgil.domain.course.domain.BaseCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseCourseRepository extends JpaRepository<BaseCourse, Long> {
}
