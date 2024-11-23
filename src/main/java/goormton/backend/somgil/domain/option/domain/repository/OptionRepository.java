package goormton.backend.somgil.domain.option.domain.repository;

import goormton.backend.somgil.domain.option.domain.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Long> {
}
