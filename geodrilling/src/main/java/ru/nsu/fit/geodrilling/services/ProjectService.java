package ru.nsu.fit.geodrilling.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.fit.geodrilling.dto.ProjectDTO;
import ru.nsu.fit.geodrilling.dto.ProjectStateDTO;
import ru.nsu.fit.geodrilling.dto.SaveProjectStateDTO;
import ru.nsu.fit.geodrilling.entity.CurveEntity;
import ru.nsu.fit.geodrilling.entity.ModelEntity;
import ru.nsu.fit.geodrilling.entity.ProjectEntity;
import ru.nsu.fit.geodrilling.entity.ProjectState;
import ru.nsu.fit.geodrilling.entity.SootEntity;
import ru.nsu.fit.geodrilling.entity.projectstate.GroupProperties;
import ru.nsu.fit.geodrilling.entity.projectstate.TabletProperties;
import ru.nsu.fit.geodrilling.entity.projectstate.enums.EnumType;
import ru.nsu.fit.geodrilling.entity.projectstate.property.EnumProperty;
import ru.nsu.fit.geodrilling.entity.projectstate.property.NumberProperty;
import ru.nsu.fit.geodrilling.exceptions.ProjectNotFoundException;
import ru.nsu.fit.geodrilling.repositories.ProjectRepository;
import ru.nsu.fit.geodrilling.repositories.ProjectStateRepository;
import ru.nsu.fit.geodrilling.repositories.SootRepository;
import ru.nsu.fit.geodrilling.repositories.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProjectService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final SootRepository sootRepository;
    private final ProjectStateRepository projectStateRepository;

    @Transactional
    public ProjectStateDTO createProjectForUser(String email, String name) {
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
        project.setModelEntity(new ModelEntity());
        project.setUser(userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")));
        ProjectState state = new ProjectState();
        TabletProperties tabletProperties = new TabletProperties();

        GroupProperties mainProperties = new GroupProperties();
        EnumProperty orientation = new EnumProperty("Ориентация", "horizontal", EnumType.ORIENTATION);
        NumberProperty startDepth = new NumberProperty("Начальная глубина", 3200.0);
        NumberProperty endDepth = new NumberProperty("Конечная глубина", 3500.0);
        mainProperties.setName("Основные свойства");
        mainProperties.setProperties(List.of(orientation, startDepth, endDepth));

        tabletProperties.setProperties(Collections.singletonList(mainProperties));
        ProjectEntity projectEntity = projectRepository.save(project);
        state.setId(projectEntity.getId());
        state.setTrackProperties(Collections.emptyList());
        state.setTabletProperties(tabletProperties);
        projectStateRepository.save(state);
        return new ProjectStateDTO(projectEntity.getId(), tabletProperties,
                state.getTrackProperties(), Collections.emptyList());
    }
    public void saveProjectState(Long projectId, SaveProjectStateDTO state) {
        ProjectState projectState = projectStateRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("У проекта нет состояния"));
        projectState.setTrackProperties(state.getTrackProperties());
        projectState.setTabletProperties(state.getTabletProperties());
        projectStateRepository.save(projectState);
    }
    public ProjectStateDTO getProjectState(Long projectId) {
        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(() ->
                new ProjectNotFoundException("Проект не найден"));
        return new ProjectStateDTO(project.getId(), project.getState().getTabletProperties(),
                project.getState().getTrackProperties(),
                project.getCurves().stream()
                        .map(CurveEntity::getDirInProject)
                        .collect(Collectors.toList()));
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

    public List<String> getProjectStructure(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(
                        () -> new NoSuchElementException("Проект с id=" + projectId + " не найден"))
                .getCurves()
                .stream()
                .map(CurveEntity::getDirInProject)
                .collect(Collectors.toList());
    }

    public void postProjectStructure(Long projectId, Map<String, String> curveDir) {
        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(
                () -> new NoSuchElementException("Проект с id=" + projectId + " не найден"));
        for (CurveEntity curve : project.getCurves()) {
            curve.setDirInProject(curveDir.get(curve.getName()));
        }
        projectRepository.save(project);
    }
}
