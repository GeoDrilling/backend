package ru.nsu.fit.geodrilling.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.fit.geodrilling.model.Token;
import ru.nsu.fit.geodrilling.model.User;

public interface TokenRepository extends JpaRepository<Token, Integer> {

  //@Query(value = "select t from Token t inner join User u on t.user.id = u.id where u.id = :id and (t.expired = false or t.revoked = false) ")
  Optional<Token> findByUser(User user);


  Optional<Token> findByAccessToken(String token);
}
