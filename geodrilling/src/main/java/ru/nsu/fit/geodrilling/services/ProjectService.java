package ru.nsu.fit.geodrilling.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.nsu.fit.geodrilling.dto.ProjectDTO;
import ru.nsu.fit.geodrilling.dto.project.CurveDTO;
import ru.nsu.fit.geodrilling.dto.project.ProjectStateRequest;
import ru.nsu.fit.geodrilling.dto.project.ProjectStateResponse;
import ru.nsu.fit.geodrilling.dto.project.TrackDTO;
import ru.nsu.fit.geodrilling.entity.ProjectEntity;
import ru.nsu.fit.geodrilling.entity.ProjectStateEntity;
import ru.nsu.fit.geodrilling.entity.SootEntity;
import ru.nsu.fit.geodrilling.entity.TrackEntity;
import ru.nsu.fit.geodrilling.entity.state.CurveStateEntity;
import ru.nsu.fit.geodrilling.repositories.ProjectRepository;
import ru.nsu.fit.geodrilling.repositories.SootRepository;
import ru.nsu.fit.geodrilling.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ProjectService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final SootRepository sootRepository;
    @Transactional
    public ProjectDTO createProjectForUser(String email, String name) {
        ProjectEntity project = new ProjectEntity();
        SootEntity sootEntity = new SootEntity();
        sootEntity.setROPLp(0);
        sootEntity.setROALp(0);
        sootEntity.setROPLDp(0);
        sootEntity.setROALDp(0);
        sootEntity.setROPLEp(0);
        sootEntity.setROALEp(0);
        sootEntity.setROPHp(0);
        sootEntity.setROAHp(0);
        sootEntity.setROAHDp(0);
        sootEntity.setROPHDp(0);
        sootEntity.setROPHEp(0);
        sootEntity.setROAHEp(0);
        sootEntity.setMdp(0);
        sootEntity.setTvdp(0);
        sootEntity.setXp(0);
        sootEntity.setZenip(0);
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

    public Boolean saveState(ProjectStateRequest request) {
        ProjectEntity project = projectRepository.findById(request.getProjectId()).orElseThrow(
                () -> new NoSuchElementException("Проект c id " + request.getProjectId() + " не найден"));
        ProjectStateEntity newState = new ProjectStateEntity(0L, new ArrayList<>());
        for (TrackDTO track : request.getTracks()) {
            TrackEntity trackEntity  = new TrackEntity();
            List<CurveStateEntity> curves = track.getCurves().stream()
                .map(x -> new CurveStateEntity(0L, x.getColor(), x.getName(), trackEntity))
                .collect(Collectors.toList());
            trackEntity.setCurvesStates(curves);
            newState.getTracks().add(trackEntity);
        }
        project.setProjectStateEntity(newState);
        projectRepository.save(project);
        return Boolean.TRUE;
    }
}
