package ru.nsu.fit.geodrilling.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.nsu.fit.geodrilling.model.Token;
import ru.nsu.fit.geodrilling.repositories.TokenRepository;
import ru.nsu.fit.geodrilling.services.auth.JwtService;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final TokenRepository tokenRepository;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    String userEmail = null;
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }
    jwt = authHeader.substring(7);
    try {
      userEmail = jwtService.extractUsername(jwt);
    } catch (Exception ignored) {
    }

    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = null;
      try {
        userDetails = userDetailsService.loadUserByUsername(userEmail);
      } catch (UsernameNotFoundException ignored) {
      }

      if (userDetails != null) {
        Boolean isTokenValid = tokenRepository.findByAccessToken(jwt)
            .map(Token::isActive)
            .orElse(false);
        if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
              userDetails,
              null,
              userDetails.getAuthorities()
          );
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authToken);
        } else {
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
      }

    }
    filterChain.doFilter(request, response);
  }
}
