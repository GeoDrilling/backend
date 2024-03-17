package ru.nsu.fit.geodrilling.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.fit.geodrilling.entity.ProjectState;

public interface ProjectStateRepository extends JpaRepository<ProjectState, Long> {
}
