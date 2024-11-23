package goormton.backend.somgil.domain.course.domain.repository;

import goormton.backend.somgil.domain.course.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
