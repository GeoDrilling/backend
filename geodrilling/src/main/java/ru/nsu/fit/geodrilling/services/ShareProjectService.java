package ru.nsu.fit.geodrilling.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.nsu.fit.geodrilling.entity.*;
import ru.nsu.fit.geodrilling.exceptions.AccessException;
import ru.nsu.fit.geodrilling.model.Constant;
import ru.nsu.fit.geodrilling.model.User;
import ru.nsu.fit.geodrilling.repositories.*;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShareProjectService {
    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;
    public String copyProject(Long projectId, User user, boolean readOnly) {
        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(() ->
                new EntityNotFoundException("Project not found"));
        if (!project.getUser().getEmail().equals(user.getEmail()))
            throw new AccessException("User is not owner");
        ProjectEntity copyProject = deepCopy(project);
        copyProject.setReadOnly(readOnly);
        copyProject.setName("Copy " + project.getName());
        copyProject = projectRepository.save(copyProject);
        return Constant.HOST + "/projects/share/" + copyProject.getId();
    }

    @Transactional
    private ProjectEntity deepCopy(ProjectEntity project) {
        ProjectEntity copyProject = projectRepository.save(new ProjectEntity());

        copyProject.getCurves().addAll(
                project.getCurves().stream().map(curve -> {
                    CurveEntity newCurve = new CurveEntity();
                    modelMapper.map(curve, newCurve);
                    newCurve.setProject(copyProject);
                    newCurve.setId(null);
                    return newCurve;
                }).collect(Collectors.toList())
        );

        SootEntity soot = new SootEntity();
        modelMapper.map(project.getSootEntity(), soot);
        soot.setId(null);
        soot.setProjectEntity(copyProject);
        copyProject.setSootEntity(soot);

        copyProject.setModelEntityList(
                project.getModelEntityList().stream().map(modelEntity -> {
                    ModelEntity newModelEntity = new ModelEntity();
                    modelMapper.map(modelEntity, newModelEntity);
                    newModelEntity.setProjectEntity(copyProject);
                    newModelEntity.setId(null);

                    newModelEntity.setAreasEntity(
                            modelEntity.getAreasEntity().stream().map(areasEntity -> {
                                AreasEntity newAreas = new AreasEntity();
                                modelMapper.map(areasEntity, newAreas);
                                newAreas.setId(null);
                                newAreas.setModelEntity(newModelEntity);
                                return newAreas;
                            }).collect(Collectors.toList())
                    );

                    newModelEntity.setCacheAreasEntity(
                            modelEntity.getCacheAreasEntity().stream().map(cacheAreasEntity -> {
                                CacheAreasEntity newCacheAreas = new CacheAreasEntity();
                                modelMapper.map(cacheAreasEntity, newCacheAreas);
                                newCacheAreas.setId(null);
                                newCacheAreas.setModelEntity(newModelEntity);
                                return newCacheAreas;
                            }).collect(Collectors.toList())
                    );
                    return newModelEntity;
                }).collect(Collectors.toList())
        );
        ProjectState state = new ProjectState();
        modelMapper.map(project.getState(), state);
        state.setId(copyProject.getId());
        copyProject.setState(state);

        return copyProject;
    }
    public Long getCopyProject(Long projectId, User user) {
        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(() ->
                new EntityNotFoundException("Project not found"));
        if (!project.getReadOnly()) {
            ProjectEntity copyProject = deepCopy(project);
            copyProject.setUser(user);
            copyProject = projectRepository.save(copyProject);
            return copyProject.getId();
        }
        return projectId;
    }
}
