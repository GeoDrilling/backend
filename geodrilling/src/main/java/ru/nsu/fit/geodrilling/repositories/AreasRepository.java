package ru.nsu.fit.geodrilling.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.fit.geodrilling.entity.AreasEntity;
import ru.nsu.fit.geodrilling.entity.ModelEntity;

public interface AreasRepository extends JpaRepository<AreasEntity, Long> {
}
