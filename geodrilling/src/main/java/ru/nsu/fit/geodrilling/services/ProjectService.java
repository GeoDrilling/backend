package ru.nsu.fit.geodrilling.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.nsu.fit.geodrilling.dto.*;
import ru.nsu.fit.geodrilling.entity.CurveEntity;
import ru.nsu.fit.geodrilling.entity.ProjectEntity;
import ru.nsu.fit.geodrilling.entity.ProjectState;
import ru.nsu.fit.geodrilling.entity.SootEntity;
import ru.nsu.fit.geodrilling.entity.projectstate.ContainerGroupProperties;
import ru.nsu.fit.geodrilling.entity.projectstate.GroupProperties;
import ru.nsu.fit.geodrilling.entity.projectstate.ModelCurveGroupProperties;
import ru.nsu.fit.geodrilling.entity.projectstate.enums.EnumType;
import ru.nsu.fit.geodrilling.entity.projectstate.property.ColorProperty;
import ru.nsu.fit.geodrilling.entity.projectstate.property.EnumProperty;
import ru.nsu.fit.geodrilling.entity.projectstate.property.GradientColor;
import ru.nsu.fit.geodrilling.entity.projectstate.property.NumberProperty;
import ru.nsu.fit.geodrilling.exceptions.ProjectNotFoundException;
import ru.nsu.fit.geodrilling.repositories.ProjectRepository;
import ru.nsu.fit.geodrilling.repositories.ProjectStateRepository;
import ru.nsu.fit.geodrilling.repositories.SootRepository;
import ru.nsu.fit.geodrilling.repositories.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

import static ru.nsu.fit.geodrilling.model.Constant.MAX;
import static ru.nsu.fit.geodrilling.model.Constant.MIN;

@Service
@AllArgsConstructor
public class ProjectService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final SootRepository sootRepository;
    private final ProjectStateRepository projectStateRepository;
    private final ModelMapper modelMapper;
    private final ModelMapService modelMapService;
    private final CurvesService curvesService;

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
        //project.setModelEntity(new ModelEntity());
        project.setUser(userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")));
        ProjectState state = new ProjectState();
        ContainerGroupProperties tabletProperties = createTabletProps();
        ContainerGroupProperties depthTrackProperties = createDepthTackProps();
        ModelCurveGroupProperties modelCurveProps = createModelCurveProps();

        ProjectEntity projectEntity = projectRepository.save(project);
        project.setState(state);
        state.setId(projectEntity.getId());
        state.setTrackProperties(Collections.emptyList());
        state.setTabletProperties(tabletProperties);
        state.setDepthTrackProperties(depthTrackProperties);
        state.setModelCurveProperties(modelCurveProps);
        projectRepository.save(project);
        projectStateRepository.save(state);
        return new ProjectStateDTO(projectEntity.getId(), tabletProperties,
                depthTrackProperties, modelCurveProps,
                state.getTrackProperties(), Collections.emptyList(), null, false);
    }

    public ModelCurveGroupProperties createModelCurveProps() {
        ModelCurveGroupProperties modelCurve = new ModelCurveGroupProperties();

        GroupProperties mainProperties = new GroupProperties();
        mainProperties.setName("Основные свойства");
        NumberProperty height = new NumberProperty("Высота", 400.0);
        NumberProperty min = new NumberProperty();
        min.setName(MIN);
        min.setValue(2292.0);
        NumberProperty max = new NumberProperty();
        max.setName(MAX);
        max.setValue(2314.0);
        NumberProperty thicknessHorizontal = new NumberProperty("Толщина границы слоёв", 2.5);
        ColorProperty colorHorizontal = new ColorProperty("Цвет границы слоёв", "#DEDEDE");
        NumberProperty thicknessVertical = new NumberProperty("Толщина границы моделей", 2.5);
        ColorProperty colorVertical = new ColorProperty("Цвет границы моделей ", "#DEDEDE");

        mainProperties.setProperties(List.of(height, max, min, thicknessHorizontal, colorHorizontal,
                thicknessVertical, colorVertical));

        modelCurve.setProperties(List.of(mainProperties));
        modelCurve.setGradient(List.of(
                new GradientColor("rgba(251,187,59,1)", 0f),
                new GradientColor("rgba(241,80,37,1)", 100f)));
        return modelCurve;
    }
    public ContainerGroupProperties createDepthTackProps() {
        ContainerGroupProperties depthTrackProperties = new ContainerGroupProperties();

        GroupProperties mainProperties = new GroupProperties();
        mainProperties.setName("Основные свойства");
        NumberProperty height = new NumberProperty("Высота", 61.0);
        NumberProperty floatingPoint = new NumberProperty("Знаков после запятой", 0.0);
        ColorProperty colorMain = new ColorProperty("Цвет текста", "#021D38");
        mainProperties.setProperties(List.of(height, colorMain, floatingPoint));


        depthTrackProperties.setProperties(List.of(mainProperties));
        return depthTrackProperties;
    }
    public ContainerGroupProperties createTabletProps() {
        ContainerGroupProperties tabletProperties = new ContainerGroupProperties();

        GroupProperties mainProperties = new GroupProperties();
        mainProperties.setName("Основные свойства");
        EnumProperty orientation = new EnumProperty("Ориентация", "horizontal", EnumType.ORIENTATION);
        NumberProperty startDepth = new NumberProperty("Начальная глубина", 3200.0);
        NumberProperty endDepth = new NumberProperty("Конечная глубина", 3500.0);
        NumberProperty scope = new NumberProperty("Масштаб", 700.0);
        mainProperties.setProperties(List.of(orientation, startDepth, endDepth,
                scope));


        GroupProperties gridStyle = new GroupProperties();
        gridStyle.setName("Свойства вертикальной сетки");
        NumberProperty interval = new NumberProperty("Шаг основных линий", 40.0);
        NumberProperty secondaryLines = new NumberProperty("Число вторичных линий", 12.0);
        NumberProperty secondaryThickness = new NumberProperty("Толщина вторичных линий", 1.0);
        NumberProperty thicknessMain = new NumberProperty("Толщина основных сетки", 2.5);
        ColorProperty colorMain = new ColorProperty("Цвет основных линий", "#DEDEDE");
        ColorProperty colorSecondary = new ColorProperty("Цвет вторичных линий", "#DEDEDE");
        gridStyle.setProperties(List.of(interval, thicknessMain, colorMain, secondaryLines, secondaryThickness, colorSecondary));

        tabletProperties.setProperties(List.of(mainProperties, gridStyle));
        return tabletProperties;
    }
    public void saveProjectState(Long projectId, SaveProjectStateDTO state) {
        ProjectState projectState = projectStateRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("У проекта нет состояния"));
        modelMapper.map(state, projectState);
        projectState.setTrackProperties(state.getTrackProperties());
        projectStateRepository.save(projectState);
    }
    public ProjectStateDTO getProjectState(Long projectId) {
        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(() ->
                new ProjectNotFoundException("Проект не найден"));
        List<ModelDTO> modelDTOList = modelMapService.mapModelDtoList(project.getModelEntityList());
        Collections.sort(modelDTOList, new Comparator<ModelDTO>() {
            @Override
            public int compare(ModelDTO o1, ModelDTO o2) {
                return Double.compare(o1.getStart(), o2.getStart());
            }
        });
        return new ProjectStateDTO(project.getId(), project.getState().getTabletProperties(),
                project.getState().getDepthTrackProperties(),
                project.getState().getModelCurveProperties(),
                project.getState().getTrackProperties(),
                curvesService.getCurvesNames(projectId).getCurvesNames(),
                modelDTOList, project.getReadOnly());
    }

    public ProjectDTO getProjectById(Long id) {
        return getProjectDTObyId(id);
    }

    public List<ProjectDTO> getProjectsByUserId(String email) {
        return getListProjectDTObyListProjectEntity(
                projectRepository.findAllByUserIdAndReadOnly(userRepository.findByEmail(email)
                        .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")).getId(), false));
    }

    public List<ProjectFrozenDTO> getProjectsFrozenByUserId(String email, Long projectId) {
        List<ProjectEntity> projectEntityList = new ArrayList<>();
        ProjectEntity project =  projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("Проект не найден"));
        Optional<ProjectEntity> optionalProject = projectRepository.findBySupplementingProject(project);
        while(optionalProject.isPresent()){
            projectEntityList.add(optionalProject.get());
            optionalProject = projectRepository.findBySupplementingProject(optionalProject.get());
        }
        return getListProjectFrozenDTObyListProjectEntity(projectEntityList);
    }

    public ProjectDTO getProjectDTObyId(Long idProject) {
        ProjectEntity projectEntity = projectRepository.findById(idProject).orElse(null);
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(projectEntity.getId());
        projectDTO.setName(projectEntity.getName());
        return projectDTO;
    }

    public List<ProjectFrozenDTO> getListProjectFrozenDTObyListProjectEntity(List<ProjectEntity> entities) {
        List<ProjectFrozenDTO> projectDTOS = new ArrayList<>();
        for (ProjectEntity projectEntity : entities) {
            ProjectFrozenDTO projectDTO = new ProjectFrozenDTO();
            projectDTO.setId(projectEntity.getId());
            projectDTO.setName(projectEntity.getName());
            projectDTO.setReadOnly(projectEntity.getReadOnly());
            projectDTO.setMaxDepth(curvesService.getDeptMax(projectEntity.getId()));
            projectDTOS.add(projectDTO);
        }
        return projectDTOS;
    }

    public List<ProjectDTO> getListProjectDTObyListProjectEntity(List<ProjectEntity> entities) {
        List<ProjectDTO> projectDTOS = new ArrayList<>();
        for (ProjectEntity projectEntity : entities) {
            ProjectDTO projectDTO = new ProjectDTO();
            projectDTO.setId(projectEntity.getId());
            projectDTO.setName(projectEntity.getName());
            projectDTO.setReadOnly(projectEntity.getReadOnly());
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

    public List<ProjectDTO> getProjectChain(Long projectId) {
        List<ProjectDTO> projectsChain = new ArrayList<>();
        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(
                () -> new NoSuchElementException("Проект с id=" + projectId + " не найден"));
        projectsChain.add(modelMapper.map(project, ProjectDTO.class));
        while ((project = project.getSupplementingProject()) != null) {
            projectsChain.add(modelMapper.map(project, ProjectDTO.class));
        }
        return projectsChain;
    }
}
