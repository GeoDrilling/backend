package ru.nsu.fit.geodrilling.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import grillid9.laslib.Curve;
import grillid9.laslib.LasReader;
import jakarta.transaction.Transactional;

import java.io.*;
import java.nio.file.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.fit.geodrilling.dto.curves.*;
import ru.nsu.fit.geodrilling.entity.CurveEntity;
import ru.nsu.fit.geodrilling.entity.ProjectEntity;
import ru.nsu.fit.geodrilling.exceptions.CurveSupplementationException;
import ru.nsu.fit.geodrilling.exceptions.NewCurvesAddingException;
import ru.nsu.fit.geodrilling.repositories.CurveRepository;
import ru.nsu.fit.geodrilling.repositories.ProjectRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurvesService {

    private final ProjectRepository projectRepository;
    private final CurveRepository curveRepository;
    private final Gson gson;

    @Transactional
    public SaveCurveDataResponse saveCurves(MultipartFile file, Long projectId) throws IOException {
        log.info("Добавление кривых в проект id={}", projectId);
        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(()
                -> new NoSuchElementException("Проект с id " + projectId + " не существует"));
        Path tempPath = Files.createTempFile(null, null);
        file.transferTo(tempPath);
        LasReader lasReader = new LasReader(tempPath.toFile().getAbsolutePath());
        lasReader.read();
        log.info("Кривых в файле {}: {}", file.getOriginalFilename(), lasReader.getCurves().size());
        // если в проекте уже есть кривые, происходит склейка по кривой DEPT
        if (!project.getCurves().isEmpty()) {
            log.info("Дополнение кривых в проекте id={}", projectId);
            Curve newDepthCurve = lasReader.getCurves().stream()
                    .filter(curve -> Objects.equals(curve.getName(), "DEPT"))
                    .findFirst().orElseThrow(
                    () -> new NoSuchElementException("Кривой DEPT не существует во входящем файле"));
            String depthInProjectData = gson.toJson(getCurveDataByName("DEPT", project.getId(), false).getCurveData());
            if (!depthInProjectData.equals(gson.toJson(newDepthCurve.getData()))) {
                throw new NewCurvesAddingException("Кривая DEPT не соответствует уже добалвенной");
            }
            lasReader.getCurves().remove(newDepthCurve);
        }
        for (Curve curve : lasReader.getCurves()) {
            project.getCurves().add(curveRepository.save(new CurveEntity(null, project, curve.getName(), gson.toJson(curve.getData()), "", false)));
        }
        log.info("Кривые добавлены в проект");
        projectRepository.save(project);
        return SaveCurveDataResponse.builder()
                .curvesNames(project.getCurves().stream().map(CurveEntity::getName).collect(Collectors.toList()))
                .build();
    }

    public GetCurvesNamesResponse getCurvesNames(Long projectId) {
        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(()
                -> new NoSuchElementException("Проект c id " + projectId + " не существует"));
        return GetCurvesNamesResponse.builder()
                .curvesNames(project.getCurves().stream().map(CurveEntity::getName).collect(Collectors.toList()))
                .build();
    }

    public CurveDataDownloadResponse getCurveDataByName(String curveName, Long projectId, Boolean isSynthetic) {
        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(()
                -> new NoSuchElementException("Проект c id " + projectId + " не существует"));
        TypeToken<List<Double>> floatListTypeToken = new TypeToken<>(){};
        return CurveDataDownloadResponse.builder()
                .curveData(gson.fromJson(curveRepository.findByNameAndProjectAndIsSynthetic(curveName, project, isSynthetic)
                        .orElseThrow(() -> new NoSuchElementException("Кривой " + curveName + " не существует в проекте"))
                        .getData(), floatListTypeToken))
                .build();
    }

    @Transactional
    public CurveSupplementationResponse supplementCurve(MultipartFile file, Long projectId) throws IOException {
        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(()
                -> new NoSuchElementException("Проект c id " + projectId + " не существует"));
        log.info("Проект " + project.getId() + ": дополнение кривых");
        Path tempPath = Files.createTempFile(null, null);
        file.transferTo(tempPath);
        LasReader lasReader = new LasReader(tempPath.toFile().getAbsolutePath());
        lasReader.read();
        if (!isCurvesMatch(project, lasReader.getCurves())) {
            throw new CurveSupplementationException("Кривые дополняющего файла не соответствуют уже добавленным кривым");
        }
        project.setReadOnly(true);
        log.info("Проект {} заморожен", project.getId());
        ProjectEntity newProject = projectRepository.save(new ProjectEntity());
        for (Curve curve : lasReader.getCurves()) {
            newProject.getCurves().add(curveRepository.save(new CurveEntity(null, newProject, curve.getName(), gson.toJson(curve.getData()), "", false)));
        }
        log.info("Дополненные кривые сохранены в новый проект с id {}", newProject.getId());
        project.setSupplementingProject(newProject);
        projectRepository.save(project);
        projectRepository.save(newProject);
        return CurveSupplementationResponse.builder()
            .curvesNames(newProject.getCurves().stream().map(CurveEntity::getName).collect(Collectors.toList()))
            .build();
    }

    public void changeRange(ProjectEntity project, String curveName , Double fromDepth, List<Double> data, Boolean isSynthetic) {
        List<Double> deptData = getCurveDataByName("DEPT", project.getId(), isSynthetic).getCurveData();
        int fromDepthIdx = Arrays.binarySearch(deptData.toArray(), fromDepth);
        if (fromDepthIdx == deptData.size()) {
            log.error("Отрезок данных выходит за пределы кривой");
            throw new RuntimeException("Отрезок данных выходит за пределы кривой");
        }
        CurveEntity curve = curveRepository.findByNameAndProjectAndIsSynthetic(curveName, project, isSynthetic).orElseThrow(
                () -> new NoSuchElementException("Кривой не сузествует")
        );
        List<Double> curveData = gson.fromJson(curve.getData(), new TypeToken<List<Double>>(){});
        for (int i = fromDepthIdx, j = 0; i < fromDepthIdx + data.size(); i++, j++) {
            curveData.set(i, data.get(j));
        }
        curve.setData(gson.toJson(curveData));
        curveRepository.save(curve);
    }

    public List<Double> getRange(ProjectEntity project, String curveName, Double fromDepth, Double toDepth, Boolean isSynthetic) {
        List<Double> deptData = getCurveDataByName("DEPT", project.getId(), isSynthetic).getCurveData();
        int fromDepthIdx = Arrays.binarySearch(deptData.toArray(), fromDepth);
        int toDepthIdx = Arrays.binarySearch(deptData.toArray(), toDepth);
        if (fromDepthIdx == deptData.size() || toDepthIdx == deptData.size()) {
            log.error("Отрезок данных выходит за пределы кривой");
            throw new RuntimeException("Отрезок данных выходит за пределы кривой");
        }
        CurveEntity curveEntity = curveRepository.findByNameAndProjectAndIsSynthetic(curveName, project, isSynthetic).orElseThrow(
                () -> new NoSuchElementException("Кривой не существует"));
        List<Double> curveData = gson.fromJson(curveEntity.getData(), new TypeToken<>(){});
        return curveData.subList(fromDepthIdx, toDepthIdx);
    }

    public CurveEntity saveSyntheticCurve(ProjectEntity project, String curveName, List<Double> data) {
        CurveEntity curve = curveRepository.save(new CurveEntity(null, project, curveName, gson.toJson(data), "synthetic/", true));
        project.getCurves().add(curve);
        projectRepository.save(project);
        return curve;
    }

    /**
     * Метод проверяет, что входящие кривые являются дополненными от исходных
     * @param project проект, содержащий исходные кривые
     * @param curves дополненные кривые
     * @return true если являются, false иначе
     */
    private boolean isCurvesMatch(ProjectEntity project, List<Curve> curves) {
        if (project.getCurves()
                .stream()
                .map(CurveEntity::getName)
                .collect(Collectors.toSet())
                .containsAll(curves.stream().map(Curve::getName).collect(Collectors.toList()))) {
            for (int i = 0; i < 3; i++) {
                CurveEntity curveInProject = project.getCurves().get(i);
                String curveData = gson.toJson(curves.stream().filter(x -> x.getName().equals(curveInProject.getName()))
                    .findFirst().get()
                    .getData());
                if (!curveData.startsWith(curveInProject.getData().substring(0, curveInProject.getData().length() - 1))) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
