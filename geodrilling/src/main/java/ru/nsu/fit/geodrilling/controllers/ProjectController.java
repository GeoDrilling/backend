package ru.nsu.fit.geodrilling.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.geodrilling.dto.UserDTO;
import ru.nsu.fit.geodrilling.entity.ProjectEntity;
import ru.nsu.fit.geodrilling.model.User;
import ru.nsu.fit.geodrilling.services.ProjectService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/project")
@AllArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ModelMapper modelMapper;
    @PostMapping
    public ResponseEntity<ProjectEntity> createProject() {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        ProjectEntity createdProject = projectService.createProjectForUser(modelMapper.map(( (UserDetails) token.getPrincipal()), UserDTO.class).getEmail());
        return ResponseEntity.ok(createdProject);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.ok().build();
    }
    @GetMapping
    public List<ProjectEntity> getProjects() {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return projectService.getProjects((User) token.getPrincipal());
    }
    @GetMapping("/{projectId}")
    public ProjectEntity getProject(@PathVariable Long projectId) {
        return projectService.getProject(projectId);
    }
}
