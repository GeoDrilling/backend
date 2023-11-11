package ru.nsu.fit.geodrilling.controllers;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.geodrilling.dto.UserDTO;
import ru.nsu.fit.geodrilling.entity.ProjectEntity;
import ru.nsu.fit.geodrilling.repositories.UserRepository;
import ru.nsu.fit.geodrilling.services.ProjectService;

@RestController
@RequestMapping("/project")
@AllArgsConstructor
public class ProjectController {

    private final ProjectService userService;
    private final ModelMapper modelMapper;
    @PostMapping("/")
    public ResponseEntity<ProjectEntity> createProject() {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        ProjectEntity createdProject = userService.createProjectForUser(modelMapper.map(( (UserDetails) token.getPrincipal()), UserDTO.class).getEmail());
        return ResponseEntity.ok(createdProject);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable Long projectId) {
        userService.deleteProject(projectId);
        return ResponseEntity.ok().build();
    }
}
