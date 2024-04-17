package ru.nsu.fit.geodrilling.advice;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.fit.geodrilling.entity.ProjectEntity;
import ru.nsu.fit.geodrilling.exceptions.FrozenProjectException;
import ru.nsu.fit.geodrilling.repositories.ProjectRepository;

import java.util.NoSuchElementException;

@Aspect
@Component
@AllArgsConstructor
@Slf4j
public class ProjectAspect {

    private final ProjectRepository projectRepository;

    @Before(value = "execution(* saveCurves(..)) && args(file, projectId)", argNames = "file,projectId")
    public void uploadLasFilePointcut(MultipartFile file, Long projectId) {
        log.info("Зашли в аспект");
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("Проекта " + projectId + " не существует"));
        if (project.getReadOnly()) {
            throw new FrozenProjectException("Проект заморожен");
        }
    }

    @Before(value = "execution(* supplementCurve(..)) && args(file, projectId)", argNames = "file,projectId")
    public void supplementLasFilePointcut(MultipartFile file, Long projectId) {
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("Проекта " + projectId + " не существует"));
        if (project.getReadOnly()) {
            throw new FrozenProjectException("Проект заморожен");
        }
    }
}


