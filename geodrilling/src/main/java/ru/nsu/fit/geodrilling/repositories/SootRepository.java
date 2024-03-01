package ru.nsu.fit.geodrilling.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.fit.geodrilling.entity.ProjectEntity;
import ru.nsu.fit.geodrilling.entity.SootEntity;

import java.util.List;

public interface SootRepository extends JpaRepository<SootEntity, Long> {


}