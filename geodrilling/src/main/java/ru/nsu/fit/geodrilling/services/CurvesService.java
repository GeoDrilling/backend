package ru.nsu.fit.geodrilling.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import grillid9.laslib.LasReader;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.InvalidContentTypeException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.fit.geodrilling.dto.CurveDto;
import ru.nsu.fit.geodrilling.dto.InterpolateDTO;
import ru.nsu.fit.geodrilling.dto.MaxMinDTO;
import ru.nsu.fit.geodrilling.dto.curves.CurveDataDownloadResponse;
import ru.nsu.fit.geodrilling.dto.curves.CurveSupplementationResponse;
import ru.nsu.fit.geodrilling.dto.curves.GetCurvesNamesResponse;
import ru.nsu.fit.geodrilling.dto.curves.SaveCurveDataResponse;
import ru.nsu.fit.geodrilling.entity.CurveEntity;
import ru.nsu.fit.geodrilling.entity.MultiCurveEntity;
import ru.nsu.fit.geodrilling.entity.ProjectEntity;
import ru.nsu.fit.geodrilling.entity.projectstate.TrackProperty;
import ru.nsu.fit.geodrilling.entity.projectstate.property.BaseProperty;
import ru.nsu.fit.geodrilling.entity.projectstate.property.NumberProperty;
import ru.nsu.fit.geodrilling.mapper.CurveMapper;
import ru.nsu.fit.geodrilling.model.Constant;
import ru.nsu.fit.geodrilling.repositories.CurveRepository;
import ru.nsu.fit.geodrilling.repositories.ProjectRepository;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurvesService {

    private final ProjectRepository projectRepository;
    private final CurveRepository curveRepository;
    private final CurveMapper curveMapper;
    private final Gson gson;



    public GetCurvesNamesResponse getCurvesNames(Long projectId) {
        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(()
                -> new NoSuchElementException("Проект c id " + projectId + " не существует"));
        GetCurvesNamesResponse response = GetCurvesNamesResponse.builder()
                .curvesNames(project.getCurves().stream()
                        .filter(curve -> !isMultiCurve(curve))
                        .map(CurveEntity::getName)
                        .collect(Collectors.toList()))
                .build();
        for (MultiCurveEntity multiCurve : project.getMultiCurves()) {
            response.getCurvesNames().add("/multicurve/" + multiCurve.getName());
        }
        return response;
    }

    public CurveDataDownloadResponse getCurveDataByName(String curveName, Long projectId, Boolean isSynthetic) {
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("Проект c id " + projectId + " не существует"));
        return getCurveDataByName(curveName, project, isSynthetic);
    }

    public CurveDataDownloadResponse getCurveDataByName(String curveName, ProjectEntity project, Boolean isSynthetic) {
        TypeToken<List<Double>> floatListTypeToken = new TypeToken<>(){};
        return CurveDataDownloadResponse.builder()
                .curveData(gson.fromJson(curveRepository.findByNameAndProjectAndIsSynthetic(curveName, project, isSynthetic)
                        .orElseThrow(() -> new NoSuchElementException("Кривой " + curveName + " не существует в проекте"))
                        .getData(), floatListTypeToken))
                .build();
    }


    public void changeRange(ProjectEntity project, String curveName , Double fromDepth, List<Double> data, Boolean isSynthetic) {
        List<Double> deptData = getCurveDataByName("DEPT", project.getId(), false).getCurveData();
        Double[] deptArray = deptData.toArray(new Double[0]);
        int fromDepthIdx = Arrays.binarySearch(deptArray, fromDepth);
        if (fromDepthIdx < 0) {
            fromDepthIdx = -fromDepthIdx - 1;
        }
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
        System.out.println(deptData.size());
        Double[] deptArray = deptData.toArray(new Double[0]);
        System.out.println(deptArray.length);
        int fromDepthIdx = Arrays.binarySearch(deptArray, fromDepth);
        int toDepthIdx = Arrays.binarySearch(deptArray, toDepth);
        System.out.println(fromDepthIdx);
        System.out.println(toDepthIdx);
        if (fromDepthIdx < 0) {
            fromDepthIdx = -fromDepthIdx - 1;
        }
        if (toDepthIdx < 0) {
            toDepthIdx = -toDepthIdx - 1;
        }
        if (toDepthIdx == deptData.size()){
            toDepthIdx = toDepthIdx -1;
        }
        System.out.println(fromDepthIdx);
        System.out.println(toDepthIdx);
        if (fromDepthIdx >= deptData.size() || toDepthIdx >= deptData.size() || fromDepthIdx > toDepthIdx) {
            System.out.println(fromDepthIdx >= deptData.size());
            System.out.println(toDepthIdx >= deptData.size());
            System.out.println(fromDepthIdx > toDepthIdx);
            log.error("Отрезок данных выходит за пределы кривой или неверно задан");
            throw new RuntimeException("Отрезок данных выходит за пределы кривой или неверно задан");
        }
        toDepthIdx = toDepthIdx + 1;
        CurveEntity curveEntity = curveRepository.findByNameAndProjectAndIsSynthetic(curveName, project, isSynthetic).orElseThrow(
                () -> new NoSuchElementException("Кривой не существует"));
        List<Double> curveData = gson.fromJson(curveEntity.getData(), new TypeToken<>(){});
        System.out.println(curveData.size());
        System.out.println(fromDepthIdx);
        System.out.println(toDepthIdx);
        System.out.println(curveData.subList(fromDepthIdx, toDepthIdx).size());
        return curveData.subList(fromDepthIdx, toDepthIdx);
    }

    public CurveEntity saveSyntheticCurve(ProjectEntity project, String curveName, List<Double> data) {
        CurveEntity curve = curveRepository.save(new CurveEntity(null, project, curveName, gson.toJson(data), "synthetic/", true));
        project.getCurves().add(curve);
        projectRepository.save(project);
        return curve;
    }

    public Double getDeptMax(Long projectId) {
        return getCurveDataByName("DEPT", projectId, false)
                .getCurveData()
                .stream()
                .max(Double::compare)
                .orElseThrow(() -> new NoSuchElementException("Кривая DEPT - пустая"));
    }

    public Double getDeptMin(Long projectId) {
        return getCurveDataByName("DEPT", projectId, false)
                .getCurveData()
                .stream()
                .min(Double::compare)
                .orElseThrow(() -> new NoSuchElementException("Кривая DEPT - пустая"));
    }
    public MaxMinDTO getCurveMaxMin(String curveName, Long projectId) {
        Double min = getCurveDataByName(curveName, projectId, false)
                .getCurveData()
                .stream()
                .min(Double::compare)
                .orElseThrow(() -> new NoSuchElementException("Кривая "+ curveName +" - пустая"));
        Double max = getCurveDataByName(curveName, projectId, false)
                .getCurveData()
                .stream()
                .max(Double::compare)
                .orElseThrow(() -> new NoSuchElementException("Кривая "+ curveName +" - пустая"));
        return new MaxMinDTO(max, min);
    }

    public void updateTvdInProjectState(Long projectId, String curveName) {
        MaxMinDTO maxMinDTO = getCurveMaxMin(curveName, projectId);
        ProjectEntity projectEntity = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("Проект c id " + projectId + " не существует"));
        List<BaseProperty> baseProperties = projectEntity.getState()
                .getModelCurveProperties()
                .getProperties()
                .get(0)
                .getProperties();
        NumberProperty minProp = (NumberProperty) baseProperties.stream()
                .filter(prop -> Objects.equals(prop.getName(), Constant.MIN))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("Свойства MIN не существует"));
        NumberProperty maxProp = (NumberProperty) baseProperties.stream()
                .filter(prop -> Objects.equals(prop.getName(), Constant.MAX))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("Свойства MAX не существует"));
        minProp.setValue(Math.round(maxMinDTO.getMin() * 10.0) / 10.0);
        maxProp.setValue(Math.round(maxMinDTO.getMax() * 10.0) / 10.0);
        projectRepository.save(projectEntity);
    }

    public void updateDeptInProjectState(Long projectId) {
        ProjectEntity projectEntity = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("Проект c id " + projectId + " не существует"));
        updateDeptInProjectState(projectEntity);
    }

    public void updateDeptInProjectState(ProjectEntity projectEntity) {
        List<BaseProperty> baseProperties = projectEntity.getState()
                .getTabletProperties()
                .getProperties()
                .get(0)
                .getProperties();
        NumberProperty startDeptProp = (NumberProperty) baseProperties.stream()
                .filter(prop -> Objects.equals(prop.getName(), "Начальная глубина"))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("Свойства \"Начальная глубина\" не существует"));
        NumberProperty endDeptProp = (NumberProperty) baseProperties.stream()
                .filter(prop -> Objects.equals(prop.getName(), "Конечная глубина"))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("Свойства \"Конечная глубина\" не существует"));
        startDeptProp.setValue(Math.round(getDeptMin(projectEntity.getId()) * 10.0) / 10.0);
        endDeptProp.setValue(Math.round(getDeptMax(projectEntity.getId()) * 10.0) / 10.0);
        projectRepository.save(projectEntity);
    }

    private void handleMultiCurves(ProjectEntity project) {
        List<MultiCurveEntity> multiCurves = getMultiCurves(project.getCurves());
        project.setMultiCurves(new ArrayList<>());
        for (MultiCurveEntity multiCurve : multiCurves) {
             project.getMultiCurves().add(multiCurve);
        }
        projectRepository.save(project);
    }

    private List<MultiCurveEntity> getMultiCurves(List<CurveEntity> curves) {
        Map<String, CurveEntity> curvesOfMultiCurves = curves.stream()
                .filter(this::isMultiCurve)
                .collect(Collectors.toMap(CurveEntity::getName, Function.identity()));

        Map<String, MultiCurveEntity> multiCurves = new HashMap<>();
        curves.stream()
                .map(curve -> getMultiCurveNameMatcher(curve.getName()))
                .filter(Matcher::find)
                .forEach(matcher -> {
                    if (!multiCurves.containsKey(matcher.group(1))) {
                        MultiCurveEntity multiCurve = new MultiCurveEntity();
                        multiCurve.setName(matcher.group(1));
                        multiCurve.setCurves(new ArrayList<>());
                        multiCurve.getCurves().add(curvesOfMultiCurves.get(matcher.group(0)));

                        multiCurves.put(matcher.group(1), multiCurve);
                    } else {
                        MultiCurveEntity multiCurve = multiCurves.get(matcher.group(1));
                        multiCurve.getCurves().add(curvesOfMultiCurves.get(matcher.group(0)));
                    }
                });
        return new ArrayList<>(multiCurves.values());
    }

    private boolean isMultiCurve(CurveEntity curveEntity) {
        return getMultiCurveNameMatcher(curveEntity.getName()).find();
    }

    private Matcher getMultiCurveNameMatcher(String name) {
        Pattern pattern = Pattern.compile("([A-Za-z]+)_(\\d+)_?([A-Za-z]+)?");
        return pattern.matcher(name);
    }

    /**
     * Метод проверяет, что входящие кривые являются дополненными от исходных
     * @param project проект, содержащий исходные кривые
     * @param curves дополненные кривые
     * @return true если являются, false иначе
     */
    private boolean isCurvesMatch(ProjectEntity project, List<CurveEntity> curves) {
        return project.getCurves()
                .stream()
                .map(CurveEntity::getName)
                .collect(Collectors.toSet())
                .containsAll(curves.stream().map(CurveEntity::getName).collect(Collectors.toList()));
    }



    public CurveDataDownloadResponse getByPathName(String name, Long projectId) {
        if (name.startsWith("/synthetic/"))
            return getCurveDataByName(name.substring(11), projectId, true);
        else
            return getCurveDataByName(name, projectId, false);
    }

    public List<CurveDto> getMultiCurve(String name, Long projectId) {
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("Проект c id " + projectId + " не существует"));
        String nameWithoutPath = name.substring(12);
        MultiCurveEntity multiCurve = project.getMultiCurves().stream()
            .filter(mCurve -> mCurve.getName().equals(nameWithoutPath))
            .findAny().orElseThrow(() -> new NoSuchElementException("Мультикривая с именем " + name + " не найдена"));
        return multiCurve.getCurves().stream()
                .map(curveMapper::map)
                .collect(Collectors.toList());
    }

    private void clearTracksInProjectState(Long projectId) {
        ProjectEntity projectEntity = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("Проект c id " + projectId + " не существует"));
        for (TrackProperty track : projectEntity.getState().getTrackProperties()) {
            track.getCurves().clear();
        }
    }

}