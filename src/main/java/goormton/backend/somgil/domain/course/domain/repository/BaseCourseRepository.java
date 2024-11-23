package goormton.backend.somgil.domain.course.domain.repository;

import goormton.backend.somgil.domain.course.domain.BaseCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BaseCourseRepository extends JpaRepository<BaseCourse, Long> {

    // 특정 packageId를 가진 BaseCourse 조회
    @Query("SELECT b FROM BaseCourse b WHERE :packageId MEMBER OF b.packageIds")
    List<BaseCourse> findByPackageId(String packageId);
}
