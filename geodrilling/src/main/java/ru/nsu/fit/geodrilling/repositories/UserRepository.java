package ru.nsu.fit.geodrilling.repositories;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.fit.geodrilling.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
  Optional<User> findByEmail(String email);

  Long getIdByEmail(String email);
}
