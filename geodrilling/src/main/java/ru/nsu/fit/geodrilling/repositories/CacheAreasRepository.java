package ru.nsu.fit.geodrilling.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.fit.geodrilling.entity.CacheAreasEntity;

import java.util.Optional;

public interface CacheAreasRepository extends JpaRepository<CacheAreasEntity, Long> {
    Optional<CacheAreasEntity> findCacheAreasEntityByParam1AndParam2AndGridFrequency
            (String param1, String param2, int gridFrequency);
}

