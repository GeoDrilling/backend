package ru.nsu.fit.geodrilling.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.fit.geodrilling.entity.SharedToken;

import java.util.Optional;

public interface SharedTokenRepository extends JpaRepository<SharedToken, Long> {
    Optional<SharedToken> findByToken(String token);
}
