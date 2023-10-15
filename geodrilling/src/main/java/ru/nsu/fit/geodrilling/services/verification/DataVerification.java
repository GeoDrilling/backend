package ru.nsu.fit.geodrilling.services.verification;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.nsu.fit.geodrilling.exceptions.LoginException;
import ru.nsu.fit.geodrilling.model.User;
import ru.nsu.fit.geodrilling.repositories.UserRepository;

@Component
@RequiredArgsConstructor
public class DataVerification {
  private final UserRepository userRepository;
  public void userVerification(User user) {
    Optional<User> userFromDb = userRepository.findByEmail(user.getEmail());
    if (userFromDb.isPresent())
      throw new LoginException("Login is already taken");

  }

}
