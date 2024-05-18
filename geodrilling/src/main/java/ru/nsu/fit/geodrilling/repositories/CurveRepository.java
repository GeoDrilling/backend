package ru.nsu.fit.geodrilling.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.fit.geodrilling.entity.CurveEntity;
import ru.nsu.fit.geodrilling.entity.ProjectEntity;

import java.util.List;
import java.util.Optional;

public interface CurveRepository extends JpaRepository<CurveEntity, Long> {

    Optional<CurveEntity> findFirstByNameAndProject(String name, ProjectEntity project);
    Optional<CurveEntity> findByNameAndProject(String name, ProjectEntity project);
    List<CurveEntity> findAllByProjectAndIsSynthetic(ProjectEntity projectEntity, Boolean isSynthetic);
    Optional<CurveEntity> findByNameAndProjectAndIsSynthetic(String name, ProjectEntity projectEntity, Boolean isSynthetic);
}
