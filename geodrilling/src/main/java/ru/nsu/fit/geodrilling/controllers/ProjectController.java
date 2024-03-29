package ru.nsu.fit.geodrilling.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.geodrilling.dto.ProjectDTO;
import ru.nsu.fit.geodrilling.dto.ProjectStateDTO;
import ru.nsu.fit.geodrilling.dto.SaveProjectStateDTO;
import ru.nsu.fit.geodrilling.dto.UserDTO;
import ru.nsu.fit.geodrilling.services.ProjectService;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/project")
@AllArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ModelMapper modelMapper;

    @PostMapping("/{name}")
    public ResponseEntity<ProjectStateDTO> createProject(@PathVariable String name) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String email = modelMapper.map(( (UserDetails) token.getPrincipal()), UserDTO.class).getEmail();
        ProjectStateDTO createdProject = projectService.createProjectForUser(email, name);
        return ResponseEntity.ok(createdProject);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        ProjectDTO project = projectService.getProjectById(id);
        if (project == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(project);
    }

    @GetMapping("/userAll")
    public ResponseEntity<List<ProjectDTO>> getProjectsByUser() {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        List<ProjectDTO> projects = projectService.getProjectsByUserId(modelMapper.map(( (UserDetails) token.getPrincipal()), UserDTO.class).getEmail());
        return ResponseEntity.ok(projects);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{projectId}/structure")
    public ResponseEntity<List<String>> getProjectStructure(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.getProjectStructure(projectId));
    }

    @PostMapping("/{projectId}/structure")
    public void postProjectStructure(@PathVariable Long projectId,
            @RequestBody Map<String, String> curveDir) {
        projectService.postProjectStructure(projectId, curveDir);
    }
    @GetMapping("/state/{projectId}")
    public ProjectStateDTO getProjectState(@PathVariable Long projectId) {
        return projectService.getProjectState(projectId);
    }
    @PutMapping("/state/{projectId}")
    public void setProjectState(@PathVariable Long projectId,
                                @RequestBody SaveProjectStateDTO state) {
        projectService.saveProjectState(projectId, state);
    }
}
