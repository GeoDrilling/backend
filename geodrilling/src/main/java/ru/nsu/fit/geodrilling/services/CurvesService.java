package ru.nsu.fit.geodrilling.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import grillid9.laslib.Curve;
import grillid9.laslib.LasReader;
import jakarta.transaction.Transactional;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
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
import ru.nsu.fit.geodrilling.repositories.ProjectRepository;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurvesService {

    @Value("${lasfile.temp-path}")
    private String tempFolderPath;

    @Value("${projects.folder-path}")
    private String projectsFolderPath;

    private final ProjectRepository projectRepository;
    private final Gson gson = new Gson();

    @Transactional
    public SaveCurveDataResponse saveCurves(MultipartFile file, Long projectId) throws IOException {
        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(()
                -> new NoSuchElementException("Проект с id " + projectId + " не существует"));
        LasReader lasReader = readLasFile(file);
        // если в проекте уже есть кривые, происходит склейка по кривой DEPT
        if (!project.getCurves().isEmpty()) {
            Curve newDepthCurve = findCurveByName("DEPT", lasReader.getCurves());
            String depthInProjectData = getCurveDataByName("DEPT", project);
            if (!depthInProjectData.equals(gson.toJson(newDepthCurve.getData()))) {
                throw new NewCurvesAddingException("Кривая DEPT не соответствует уже добалвенной кривой");
            }
            lasReader.getCurves().remove(newDepthCurve);
        }
        File projectDataFolder = initProjectDataFolder(projectId);
        for (Curve curve : lasReader.getCurves()) {
            if (project.getCurves().stream().filter(x -> x.getName().equals(curve.getName())).findAny().isEmpty()) {
                File curveData = new File(
                    projectDataFolder.getAbsolutePath() + "\\" + curve.getName());
                curveData.createNewFile();
                saveCurveDataTo(curveData, curve, project);
            }
        }
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

    public CurveDataDownloadResponse getCurveDataByName(String curveName, Long projectId) {
        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(()
                -> new NoSuchElementException("Проект c id " + projectId + " не существует"));
        TypeToken<List<Double>> floatListTypeToken = new TypeToken<>(){};
        return CurveDataDownloadResponse.builder()
                .curveData(gson.fromJson(getCurveDataByName(curveName, project), floatListTypeToken))
                .build();
    }

    @Transactional
    public CurveSupplementationResponse supplementCurve(MultipartFile file, Long projectId) throws IOException {
        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(()
                -> new NoSuchElementException("Проект c id " + projectId + " не существует"));
        log.info("Проект " + project.getId() + ": дополнение кривых");
        LasReader lasReader = readLasFile(file);
        if (!isCurvesMatch(project, lasReader.getCurves())) {
            throw new CurveSupplementationException("Кривые дополняющего файла не соответствуют уже добавленным кривым");
        }
        project.setReadOnly(true);
        log.info("Проект {} заморожен", project.getId());
        ProjectEntity newProject = projectRepository.save(new ProjectEntity()); // проверить
        File newProjectDataFolder = initProjectDataFolder(newProject.getId());
        for (Curve curve : lasReader.getCurves()) {
            saveCurveDataTo(new File(newProjectDataFolder + "\\" + curve.getName()),
                curve, newProject);
        }
        log.info("Дополненные кривые сохранены в новый проект с id {}", newProject.getId());
        project.setSupplementingProject(newProject);
        projectRepository.save(project);
        projectRepository.save(newProject);
        return CurveSupplementationResponse.builder()
            .curvesNames(getCurvesNames(lasReader.getCurves()))
            .build();
    }

    public void changeRange(ProjectEntity project, String curveName , Double fromDepth, List<Double> data) {
        List<Double> deptData = getCurveDataByName("DEPT", project.getId()).getCurveData();
        int fromDepthIdx = deptData.indexOf(fromDepth);
        List<Double> curveData = getCurveDataByName(curveName, project.getId()).getCurveData();
        for (int i = fromDepthIdx, j = 0; i < fromDepthIdx + data.size(); i++, j++) {
            curveData.set(i, data.get(j));
        }
        CurveEntity curveEntity = project.getCurves()
                .stream()
                .filter(curve -> curve.getName().equals(curveName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Кривой " + curveName + " не существует"));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(curveEntity.getDataFile()))) {
            writer.write(gson.toJson(curveData));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public List<Double> getRange(ProjectEntity project, String curveName, Double fromDepth, Double toDepth) {
        List<Double> deptData = getCurveDataByName("DEPT", project.getId()).getCurveData();
        int fromDepthIdx = deptData.indexOf(fromDepth);
        int toDepthIdx = deptData.indexOf(toDepth);
        List<Double> curveData = getCurveDataByName(curveName, project.getId()).getCurveData();
        return curveData.subList(fromDepthIdx, toDepthIdx);
    }

    private void saveCurveDataTo(File curveData, Curve curve, ProjectEntity project) throws IOException {
        CurveEntity curveEntity = CurveEntity.builder()
            .dataFile(curveData)
            .name(curve.getName())
            .project(project)
            .dirInProject(curve.getName())
            .build();
        project.getCurves().add(curveEntity);
        try (FileWriter writer = new FileWriter(curveData)) {
            writer.write(gson.toJson(curve.getData()));
        }
    }

    /**
     * Метод проверяет, что входящие кривые являются дополненными от исходных
     * @param project проект, содержащий исходные кривые
     * @param curves дополненные кривые
     * @return true если являются, false иначе
     */
    private boolean isCurvesMatch(ProjectEntity project, List<Curve> curves) {
        if (new HashSet<>(getCurveEntitiesNames(project.getCurves()))
                .containsAll(getCurvesNames(curves))) {
            for (int i = 0; i < 3; i++) {
                String curveName = project.getCurves().get(i).getName();
                String curveEntityData = getCurveDataByName(curveName, project);
                String curveData = gson.toJson(curves.stream().filter(x -> x.getName().equals(curveName))
                    .findFirst().get()
                    .getData());
                if (!curveData.startsWith(curveEntityData.substring(0, curveEntityData.length() - 1))) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private List<String> getCurveEntitiesNames(List<CurveEntity> curveEntities) {
        return curveEntities.stream().map(CurveEntity::getName).collect(Collectors.toList());
    }

    private List<String> getCurvesNames(List<Curve> curves) {
        return curves.stream().map(Curve::getName).collect(Collectors.toList());
    }

    /**
     * @param file Входной файл
     * @return созданный временный файл со случайным именем и содеджимым входного файла
     * @throws IOException если не получилось создать временный файл
     */
    private File createTempFile(MultipartFile file) throws IOException {
        Random random = new Random();
        Files.createDirectories(Paths.get(tempFolderPath));
        File tempFile = new File(tempFolderPath + "\\" + random.nextLong());
        file.transferTo(tempFile);
        return tempFile;
    }

    /**
     * @param file Входной файл
     * @return LoveLas.LasReader
     * @throws IOException если неудалось прочитать файл
     */
    private LasReader readLasFile(MultipartFile file) throws IOException {
        File tempFile = createTempFile(file);
        LasReader lasReader = new LasReader(tempFile.getAbsolutePath());
        lasReader.read();
        tempFile.delete();
        return lasReader;
    }

    private File initProjectDataFolder(Long projectId) {
        File projectDataFolder = new File(projectsFolderPath + "\\project" + projectId
                + "\\data");
        projectDataFolder.mkdirs();
        log.info("Директория: {} создана", projectDataFolder);
        return projectDataFolder;
    }

    /**
     * @param curveName Имя искомой кривой
     * @param curves Список кривых, хранящихся на сервере
     * @return данные искомой кривой
     */
    private CurveEntity findCurveEntityByName(String curveName, List<CurveEntity> curves) {
        for (CurveEntity curve : curves) {
            if (curve.getName().equals(curveName)) {
                return curve;
            }
        }
        throw new NoSuchElementException("Кривой " + curveName
                + " не существует");
    }

    /**
     * @param curveName Имя искомой кривой
     * @param curves Список кривых, полученных бибилиотекой LoveLas
     * @return данные искомой кривой
     */
    private Curve findCurveByName(String curveName, List<Curve> curves) {
        for (Curve curve : curves) {
            if (curve.getName().equals(curveName)) {
                return curve;
            }
        }
        throw new NoSuchElementException("Кривой " + curveName
                + " не существует");
    }

    private String getCurveDataByName(String name, ProjectEntity project) {
        File dataFile = findCurveEntityByName(name, project.getCurves()).getDataFile();
        String curveDataInJson;
        try {
            curveDataInJson = new String(Files.readAllBytes(Paths.get(dataFile.getAbsolutePath())));
        } catch (IOException e) {
            throw new RuntimeException("Невозможно считать данные кривой");
        }
        return curveDataInJson;
    }
}
