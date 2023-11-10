package ru.nsu.fit.geodrilling.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.geodrilling.dto.AuthenticationRequest;
import ru.nsu.fit.geodrilling.dto.AuthenticationResponse;
import ru.nsu.fit.geodrilling.dto.RegisterRequest;
import ru.nsu.fit.geodrilling.services.auth.AuthenticationService;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody RegisterRequest registerRequest,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authenticationService.register(registerRequest, response));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request, response));
    }

    @PostMapping("/refresh-token")
    public AuthenticationResponse refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return authenticationService.refreshToken(request, response);
    }
}
