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
import ru.nsu.fit.geodrilling.dto.MaxMinDTO;
import ru.nsu.fit.geodrilling.dto.InterpolateDTO;
import ru.nsu.fit.geodrilling.dto.curves.CurveDataDownloadResponse;
import ru.nsu.fit.geodrilling.dto.curves.CurveSupplementationResponse;
import ru.nsu.fit.geodrilling.dto.curves.GetCurvesNamesResponse;
import ru.nsu.fit.geodrilling.dto.curves.SaveCurveDataResponse;
import ru.nsu.fit.geodrilling.entity.CurveEntity;
import ru.nsu.fit.geodrilling.entity.ProjectEntity;
import ru.nsu.fit.geodrilling.entity.projectstate.TrackProperty;
import ru.nsu.fit.geodrilling.entity.projectstate.property.BaseProperty;
import ru.nsu.fit.geodrilling.entity.projectstate.property.NumberProperty;
import ru.nsu.fit.geodrilling.exceptions.NewCurvesAddingException;
import ru.nsu.fit.geodrilling.model.Constant;
import ru.nsu.fit.geodrilling.repositories.CurveRepository;
import ru.nsu.fit.geodrilling.repositories.ProjectRepository;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurvesService {

    private final ProjectRepository projectRepository;
    private final CurveRepository curveRepository;
    private final Gson gson;
    private final ProjectService projectService;
    private final InterpolationService interpolationService;

    @Transactional
    public SaveCurveDataResponse save(MultipartFile file, Long projectId) throws IOException {
        log.info("Добавление кривых в проект id={}", projectId);
        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(()
                -> new NoSuchElementException("Проект с id " + projectId + " не существует"));
        List<CurveEntity> curves;
        CurveEntity newDepthCurve;
        if (file.getContentType().equals("text/csv")) {
            curves = fromCsv(file);
            newDepthCurve = curves.stream()
                    .filter(curve -> Objects.equals(curve.getName(), "MD"))
                    .findFirst().orElseThrow(
                            () -> new NoSuchElementException("Кривой DEPT не существует во входящем файле"));
            newDepthCurve.setName("DEPT");

        } else if (file.getContentType().equals("application/octet-stream")) {
            curves = fromLas(file);
            newDepthCurve = curves.stream()
                    .filter(curve -> Objects.equals(curve.getName(), "DEPT"))
                    .findFirst().orElseThrow(
                            () -> new NoSuchElementException("Кривой DEPT не существует во входящем файле"));
        } else {
            log.error("Расширение файла {} не поддерживается!", file.getContentType());
            throw new InvalidContentTypeException("Расширение файла " + file.getContentType() + " не поддерживается!");
        }
        log.info("123");
        // если в проекте уже есть кривые, происходит склейка по кривой DEPT
        if (!project.getCurves().isEmpty()) {
            log.info("Склейка кривых в проекте id={}", projectId);

//            String depthInProjectData = gson.toJson(getCurveDataByName("DEPT", project.getId(), false).getCurveData());
//            if (!depthInProjectData.equals(newDepthCurve.getData())) {
//                throw new NewCurvesAddingException("Кривая DEPT не соответствует уже добалвенной");
//            }

            //nik
            for (CurveEntity с : curves) {
                System.out.println(newDepthCurve.getName());
            }


            TypeToken<List<Double>> floatListTypeToken = new TypeToken<>(){};

            double[] DeptNewInArray = CurveDataDownloadResponse.builder()
                    .curveData(gson.fromJson(newDepthCurve
                            .getData(), floatListTypeToken))
                    .build().getCurveData().stream().mapToDouble(Double::doubleValue).toArray();

            curves.remove(newDepthCurve);


            List<CurveEntity> curvesInProject = project.getCurves();
            CurveEntity Dept = curvesInProject.stream()
                    .filter(curve -> Objects.equals(curve.getName(), "DEPT"))
                    .findFirst().orElseThrow(
                            () -> new NoSuchElementException("123"));

            double[] DeptInArray = CurveDataDownloadResponse.builder()
                    .curveData(gson.fromJson(Dept
                            .getData(), floatListTypeToken))
                    .build().getCurveData().stream().mapToDouble(Double::doubleValue).toArray();

            curvesInProject.remove(Dept);

            List<String> names = new ArrayList<>();
            for (CurveEntity curve :  curves){
                names.add(curve.getName());
            }

            for (CurveEntity curve :  curvesInProject){
                names.add(curve.getName());
            }

            for (String name :  names){
                System.out.println(name);
            }


            List<double []> curvesInProjectInArray =   curvesInProject.stream()
                    .map(list -> CurveDataDownloadResponse.builder()
                            .curveData(gson.fromJson(list
                                    .getData(), floatListTypeToken))
                            .build().getCurveData().stream()
                            .mapToDouble(Double::doubleValue)
                            .toArray())
                    .collect(Collectors.toList());


            List<double []> curvesNewInArray =   curves.stream()
                    .map(list -> CurveDataDownloadResponse.builder()
                            .curveData(gson.fromJson(list
                                    .getData(), floatListTypeToken))
                            .build().getCurveData().stream()
                            .mapToDouble(Double::doubleValue)
                            .toArray())
                    .collect(Collectors.toList());

            InterpolateDTO interpolateDTO = interpolationService.interpolateDepths(DeptNewInArray, curvesNewInArray, DeptInArray, curvesInProjectInArray);

            project.getCurves().clear();
            curves.clear();

            curves.add(new CurveEntity(null, project, "DEPT", gson.toJson(interpolateDTO.getDepth()), "", false));
            for(int i =0; i < interpolateDTO.getCurves().size(); i++){
                curves.add(new CurveEntity(null, project, names.get(i), gson.toJson(interpolateDTO.getCurves().get(i)), "", false));
            }
        }

        for (CurveEntity curve : curves) {
            curve.setProject(project);
            project.getCurves().add(curveRepository.save(curve));
        }
        log.info("Кривые добавлены в проект");
        projectRepository.save(project);
        try {
            updateDeptInProjectState(projectId);
        } catch (Exception e) {
            log.warn("Кривой DEPT нет в добавленных кривых");
        }

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
        log.info("Проект " + projectId + ": дополнение кривых");
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("Проект не найден"));
        ProjectEntity newProject = projectRepository.findById(projectService
                        .createProjectForUser(project.getUser().getEmail(), project.getName()).getId())
                .orElseThrow(() -> new NoSuchElementException("Не найден новый проект"));
        save(file, newProject.getId());
        log.info("Дополненные кривые сохранены в новый проект с id {}", newProject.getId());
        project.setReadOnly(true);
        log.info("Проект {} заморожен", project.getId());
        project.setSupplementingProject(newProject);
        return CurveSupplementationResponse.builder()
            .curvesNames(newProject.getCurves().stream().map(CurveEntity::getName).collect(Collectors.toList()))
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
        Double[] deptArray = deptData.toArray(new Double[0]);
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
        System.out.println(fromDepthIdx);
        System.out.println(toDepthIdx);
        if (fromDepthIdx >= deptData.size() || toDepthIdx >= deptData.size() || fromDepthIdx > toDepthIdx) {
            System.out.println(fromDepthIdx >= deptData.size());
            System.out.println(toDepthIdx >= deptData.size());
            System.out.println(fromDepthIdx > toDepthIdx);
            log.error("Отрезок данных выходит за пределы кривой или неверно задан");
            throw new RuntimeException("Отрезок данных выходит за пределы кривой или неверно задан");
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
        startDeptProp.setValue(Math.round(getDeptMin(projectId) * 10.0) / 10.0);
        endDeptProp.setValue(Math.round(getDeptMax(projectId) * 10.0) / 10.0);
        projectRepository.save(projectEntity);
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

    public List<CurveEntity> fromLas(MultipartFile file) {
        try {
            Path tempPath = Files.createTempFile(null, null);
            file.transferTo(tempPath);
            LasReader lasReader = new LasReader(tempPath.toFile().getAbsolutePath());
            lasReader.read();
            return lasReader.getCurves()
                    .stream()
                    .map(x -> CurveEntity.builder().name(x.getName()).data(gson.toJson(x.getData())).build())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public List<CurveEntity> fromCsv(MultipartFile file) {
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] curvesNames = reader.readNext();
            Map<String, List<Double>> nameToData  = new HashMap<>();
            for (String curveName : curvesNames) {
                nameToData.put(curveName, new ArrayList<>());
            }
            String[] dataLine;
            while ((dataLine = reader.readNext()) != null) {
                for (int i = 0; i < curvesNames.length; i++) {
                    nameToData.get(curvesNames[i]).add(Double.parseDouble(dataLine[i]));
                }
            }
            List<CurveEntity> curves = new ArrayList<>(curvesNames.length);
            for (int i = 0; i < curvesNames.length; i++) {
                curves.add(CurveEntity.builder().name(curvesNames[i]).data(gson.toJson(nameToData.get(curvesNames[i]))).build());
            }
            return curves;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }

    public CurveDataDownloadResponse getByPathName(String name, Long projectId) {
        if (name.startsWith("/synthetic/"))
            return getCurveDataByName(name.substring(11), projectId, true);
        else
            return getCurveDataByName(name, projectId, false);
    }

    private void clearTracksInProjectState(Long projectId) {
        ProjectEntity projectEntity = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("Проект c id " + projectId + " не существует"));
        for (TrackProperty track : projectEntity.getState().getTrackProperties()) {
            track.getCurves().clear();
        }
    }

}