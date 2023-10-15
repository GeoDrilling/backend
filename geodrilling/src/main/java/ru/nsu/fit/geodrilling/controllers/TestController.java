package ru.nsu.fit.geodrilling.controllers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.fit.geodrilling.dto.UserDTO;

@RestController
@RequiredArgsConstructor
public class TestController {
  private final ModelMapper modelMapper;
  @GetMapping("/demo")
  public UserDTO demo() {
    UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    return modelMapper.map(( (UserDetails) token.getPrincipal()), UserDTO.class);
  }
}
