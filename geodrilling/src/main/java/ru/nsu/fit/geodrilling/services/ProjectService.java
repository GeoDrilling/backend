package ru.nsu.fit.geodrilling.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.fit.geodrilling.dto.ProjectDTO;
import ru.nsu.fit.geodrilling.entity.ProjectEntity;
import ru.nsu.fit.geodrilling.entity.SootEntity;
import ru.nsu.fit.geodrilling.repositories.ProjectRepository;
import ru.nsu.fit.geodrilling.repositories.SootRepository;
import ru.nsu.fit.geodrilling.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ProjectService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final SootRepository sootRepository;
    @Transactional
    public ProjectDTO createProjectForUser(String email, String name) {
        ProjectEntity project = new ProjectEntity();
        SootEntity sootEntity = new SootEntity();
        sootRepository.save(sootEntity);
        project.setName(name);
        project.setSootEntity(sootEntity);
        project.setUser(userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")));
        projectRepository.save(project);
        return getProjectDTObyId(project.getId());
    }

    public ProjectDTO getProjectById(Long id) {
        return getProjectDTObyId(id);
    }

    public List<ProjectDTO> getProjectsByUserId(String email) {
        return getListProjectDTObyListProjectEntity(
                projectRepository.findAllByUserId(userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")).getId()));
    }

    public ProjectDTO getProjectDTObyId(Long idProject) {
        ProjectEntity projectEntity = projectRepository.findById(idProject).orElse(null);
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(projectEntity.getId());
        projectDTO.setName(projectEntity.getName());
        return projectDTO;
    }

    public List<ProjectDTO> getListProjectDTObyListProjectEntity(List<ProjectEntity> entities) {
        List<ProjectDTO> projectDTOS = new ArrayList<>();
        for (ProjectEntity projectEntity : entities) {
            ProjectDTO projectDTO = new ProjectDTO();
            projectDTO.setId(projectEntity.getId());
            projectDTO.setName(projectEntity.getName());
            projectDTOS.add(projectDTO);
        }
        return projectDTOS;
    }

    @Transactional
    public void deleteProject(Long projectId) {
        projectRepository.deleteById(projectId);
    }
}
