package ru.nsu.fit.geodrilling.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.fit.geodrilling.entity.ModelEntity;
import ru.nsu.fit.geodrilling.entity.ProjectEntity;

public interface ModelRepository extends JpaRepository<ModelEntity, Long> {
  List<ModelEntity> findByProjectEntityOrderByStartXAsc(ProjectEntity projectEntity);
  List<ModelEntity> findByProjectEntityOrderByStartXDesc(ProjectEntity projectEntity);
}
