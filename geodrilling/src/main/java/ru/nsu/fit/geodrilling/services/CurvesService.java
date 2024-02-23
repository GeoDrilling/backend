package ru.nsu.fit.geodrilling.services;

import com.google.gson.Gson;
import grillid9.laslib.Curve;
import grillid9.laslib.LasReader;
import jakarta.transaction.Transactional;
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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

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
        return CurveDataDownloadResponse.builder()
                .curveDataInJson(getCurveDataByName(curveName, project))
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
        deleteAllCurves(project);
        log.info("Проект " + project.getId() + ": кривые проекта удалены");
        for (Curve curve : lasReader.getCurves()) {
            saveCurveDataTo(new File(projectsFolderPath + "\\project"
                    + projectId + "\\data\\" + curve.getName()),
                curve, project);
        }
        projectRepository.save(project);
        log.info("Проект " + project.getId() + ": дополненные кривые добавлены");
        return CurveSupplementationResponse.builder()
            .curvesNames(getCurvesNames(lasReader.getCurves()))
            .build();
    }

    private void saveCurveDataTo(File curveData, Curve curve, ProjectEntity project) throws IOException {
        CurveEntity curveEntity = CurveEntity.builder()
            .dataFile(curveData)
            .name(curve.getName())
            .project(project)
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

    private void deleteAllCurves(ProjectEntity project) {
        String curvesDataDir = projectsFolderPath + "\\" + "project"
            + project.getId() + "\\data\\";
        List<String> curvesNames = getCurveEntitiesNames(project.getCurves());
        project.getCurves().clear();
        projectRepository.save(project);
        for (String name : curvesNames) {
            File curveDataFile = new File(curvesDataDir + name);
            curveDataFile.delete();
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
        try {
            Random random = new Random();
            File tempFile = new File(tempFolderPath + "\\" + random.nextLong());
            tempFile.mkdirs();
            file.transferTo(tempFile);
            return tempFile;
        } catch (IOException | SecurityException e) {
            throw new IOException("Невозможно создать временный файл");
        }
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
