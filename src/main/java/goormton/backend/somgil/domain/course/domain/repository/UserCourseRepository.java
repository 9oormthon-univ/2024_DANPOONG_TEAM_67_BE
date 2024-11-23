package goormton.backend.somgil.domain.course.domain.repository;

import goormton.backend.somgil.domain.course.domain.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {
}
