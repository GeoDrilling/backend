package ru.nsu.fit.geodrilling.services.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.nsu.fit.geodrilling.dto.AuthenticationRequest;
import ru.nsu.fit.geodrilling.dto.AuthenticationResponse;
import ru.nsu.fit.geodrilling.dto.RegisterRequest;
import ru.nsu.fit.geodrilling.dto.UserDTO;
import ru.nsu.fit.geodrilling.exceptions.InvalidJWT;
import ru.nsu.fit.geodrilling.exceptions.LoginException;
import ru.nsu.fit.geodrilling.model.Role;
import ru.nsu.fit.geodrilling.model.Token;
import ru.nsu.fit.geodrilling.model.User;
import ru.nsu.fit.geodrilling.repositories.TokenRepository;
import ru.nsu.fit.geodrilling.repositories.UserRepository;
import ru.nsu.fit.geodrilling.services.verification.DataVerification;


@Slf4j
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

  @Value("${application.security.jwt.refresh-token.expiration}")
  private int refreshExpiration;
  public AuthenticationResponse register(RegisterRequest userDetailsDTO, HttpServletResponse response) {
    userDetailsDTO.setPassword(passwordEncoder.encode(userDetailsDTO.getPassword()));
    User user = mapper.map(userDetailsDTO, User.class);
    user.setRole(Role.USER);
    dataVerification.userVerification(user);

    user = userRepository.save(user);
    return generateTokensAndAuthenticate(user, response);
  }
  public AuthenticationResponse authenticate(AuthenticationRequest userDetailsDTO, HttpServletResponse response) {
    try {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDetailsDTO.getEmail(),
                        userDetailsDTO.getPassword()
                )
        );
    } catch (AuthenticationException ignored) {
        throw new LoginException("Incorrect login or password");
    }

    User user = userRepository.findByEmail(userDetailsDTO.getEmail())
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    return generateTokensAndAuthenticate(user, response);
  }
  private AuthenticationResponse generateTokensAndAuthenticate(User user, HttpServletResponse response) {
    String accessToken = jwtService.generateAccessToken(user);
    String refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(user, accessToken, refreshToken);
    setCookie(response, refreshToken);
    UserDTO userDTO = mapper.map(user, UserDTO.class);
    return new AuthenticationResponse(accessToken, refreshToken, userDTO);
  }
  private void setCookie(HttpServletResponse response, String refresh) {
    response.setStatus(HttpServletResponse.SC_OK);
    Cookie cookie = new Cookie("refresh", refresh);
    cookie.setHttpOnly(true);
    cookie.setMaxAge(refreshExpiration);
    response.addCookie(cookie);
  }

  private void saveUserToken(User user, String accessToken, String refreshToken) {
    Token token = tokenRepository.findByUser(user).orElse(
        Token.builder()
            .user(user)
            .active(true)
            .build());
    token.setAccessToken(accessToken);
    token.setRefreshToken(refreshToken);
    token.setActive(true);
    user.setToken(token);
    tokenRepository.save(token);
  }

  public AuthenticationResponse refreshToken(
      HttpServletRequest request,
      String refreshToken
  ) {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String userEmail;
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      refreshToken = authHeader.substring(7);
    } else if (refreshToken == null) {
      throw new InvalidJWT("Missing JWT");
    }
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
            .user(mapper.map(user, UserDTO.class))
            .build();
      }
    }
    throw new InvalidJWT("Incorrect JWT");
  }
}
