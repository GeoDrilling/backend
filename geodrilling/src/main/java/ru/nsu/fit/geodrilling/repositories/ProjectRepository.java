package ru.nsu.fit.geodrilling.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.fit.geodrilling.entity.ProjectEntity;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    Optional<ProjectEntity> findBySupplementingProject(ProjectEntity projectEntity);
    List<ProjectEntity> findAllByUserId(Long userId);
    List<ProjectEntity> findAllByUserIdAndReadOnly(Long userId, Boolean readOnly);


}
