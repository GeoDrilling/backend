package ru.nsu.fit.geodrilling.controllers;

import java.util.List;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.geodrilling.dto.ModelDTO;
import ru.nsu.fit.geodrilling.dto.SaveModelResponse;
import ru.nsu.fit.geodrilling.dto.UserDTO;
import ru.nsu.fit.geodrilling.services.ModelService;
import ru.nsu.fit.geodrilling.services.UserService;

@RestController
@RequestMapping("/model")
@AllArgsConstructor
public class ModelController {

  private final UserService userService;
  private final ModelService modelService;
  private final ModelMapper modelMapper;

  @PostMapping("/create")
  public ResponseEntity<ModelDTO> createModel(
      @RequestBody ModelDTO modelDTO,
      @RequestParam("project_id") Long idProject,
      @RequestParam("start") Double Start, @RequestParam("end") Double End) {
    UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext()
        .getAuthentication();
    String email = (modelMapper.map(((UserDetails) token.getPrincipal()), UserDTO.class)
        .getEmail());
    return ResponseEntity.ok(modelService.createModel(email, modelDTO, idProject));
  }

  @GetMapping("/createStartModel")
  public ResponseEntity<ModelDTO> createStartModel(
      @RequestParam("project_id") Long idProject, @RequestParam("start") Double Start,
      @RequestParam("end") Double End
  ) {
    UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext()
        .getAuthentication();
    String email = (modelMapper.map(((UserDetails) token.getPrincipal()), UserDTO.class)
        .getEmail());
    return ResponseEntity.ok(modelService.createStartModel(email, idProject, Start, End));
  }

  @PostMapping("/saveModel")
  public ResponseEntity<SaveModelResponse> saveModel(
      @RequestBody ModelDTO modelDTO,
      @RequestParam("project_id") Long idProject
  ) {
    UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext()
        .getAuthentication();
    String email = (modelMapper.map(((UserDetails) token.getPrincipal()), UserDTO.class)
        .getEmail());
    return ResponseEntity.ok(modelService.saveModel(email, modelDTO, idProject));
  }

  @GetMapping("/getModel")
  public ResponseEntity<List<ModelDTO>> getModel(
      @RequestParam("project_id") Long idProject
  ) {
    UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext()
        .getAuthentication();
    String email = (modelMapper.map(((UserDetails) token.getPrincipal()), UserDTO.class)
        .getEmail());
    return ResponseEntity.ok(modelService.getModel(email, idProject));
  }
}
