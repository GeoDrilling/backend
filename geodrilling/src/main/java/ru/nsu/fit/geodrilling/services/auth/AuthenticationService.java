package ru.nsu.fit.geodrilling.services.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.nsu.fit.geodrilling.dto.AuthenticationRequest;
import ru.nsu.fit.geodrilling.dto.AuthenticationResponse;
import ru.nsu.fit.geodrilling.dto.RegisterRequest;
import ru.nsu.fit.geodrilling.exceptions.InvalidJWT;
import ru.nsu.fit.geodrilling.model.Role;
import ru.nsu.fit.geodrilling.model.Token;
import ru.nsu.fit.geodrilling.model.User;
import ru.nsu.fit.geodrilling.repositories.TokenRepository;
import ru.nsu.fit.geodrilling.repositories.UserRepository;
import ru.nsu.fit.geodrilling.services.verification.DataVerification;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository userRepository;
  private final ModelMapper mapper;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final DataVerification dataVerification;
  private final TokenRepository tokenRepository;
  public AuthenticationResponse register(RegisterRequest userDetailsDTO) {
    userDetailsDTO.setPassword(passwordEncoder.encode(userDetailsDTO.getPassword()));
    User user = mapper.map(userDetailsDTO, User.class);
    user.setRole(Role.USER);
    dataVerification.userVerification(user);
    userRepository.save(user);
    String accessToken = jwtService.generateAccessToken(user);
    String refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(user, accessToken, refreshToken);
    return new AuthenticationResponse(accessToken, refreshToken);
  }
  public AuthenticationResponse authenticate(AuthenticationRequest userDetailsDTO) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            userDetailsDTO.getEmail(),
            userDetailsDTO.getPassword()
        )
    );
    User user = userRepository.findByEmail(userDetailsDTO.getEmail())
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    String accessToken = jwtService.generateAccessToken(user);
    String refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(user, accessToken, refreshToken);
    return new AuthenticationResponse(accessToken, refreshToken);
  }

  private void saveUserToken(User user, String accessToken, String refreshToken) {
    Token token = tokenRepository.findByUser(user).orElse(
        Token.builder()
            .user(user)
            .active(true)
            .build());
    token.setAccessToken(accessToken);
    token.setRefreshToken(refreshToken);
    user.setToken(token);
    tokenRepository.save(token);
  }

  public AuthenticationResponse refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      throw new InvalidJWT("Missing JWT");
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      User user = userRepository.findByEmail(userEmail)
          .orElseThrow(() -> new UsernameNotFoundException("User not found"));
      if (jwtService.isTokenValid(refreshToken, user) &&
          user.getToken().isActive() &&
          user.getToken().getRefreshToken().equals(refreshToken)) {
        String accessToken = jwtService.generateAccessToken(user);
        saveUserToken(user, accessToken, refreshToken);
        return AuthenticationResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
      }
    }
    throw new InvalidJWT("Incorrect JWT");
  }
}
