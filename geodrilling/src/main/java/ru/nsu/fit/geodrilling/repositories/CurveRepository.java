package ru.nsu.fit.geodrilling.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.fit.geodrilling.entity.CurveEntity;
import ru.nsu.fit.geodrilling.entity.ProjectEntity;

import java.util.Optional;

public interface CurveRepository extends JpaRepository<CurveEntity, Long> {

    Optional<CurveEntity> findFirstByNameAndProject(String name, ProjectEntity project);
    Optional<CurveEntity> findByNameAndProjectAndIsSynthetic(String name, ProjectEntity projectEntity, Boolean isSynthetic);
}
