package ru.nsu.fit.geodrilling.services.file;

import com.google.gson.Gson;
import grillid9.laslib.Curve;
import grillid9.laslib.LasReader;

import java.io.*;
import java.util.*;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.fit.geodrilling.dto.curves.CurveDataDownloadResponse;
import ru.nsu.fit.geodrilling.dto.curves.GetCurvesNamesResponse;
import ru.nsu.fit.geodrilling.dto.curves.LasFileUploadResponse;
import ru.nsu.fit.geodrilling.entity.CurveEntity;
import ru.nsu.fit.geodrilling.entity.ProjectEntity;
import ru.nsu.fit.geodrilling.repositories.ProjectRepository;
import ru.nsu.fit.geodrilling.utils.FileUtil;

@Service
@RequiredArgsConstructor
public class LasFileService {

    private final ProjectRepository projectRepository;
    private final FileUtil fileUtil;

    @Value("${lasfile.temp-path}")
    private String pathToTempFolder;

    @Value("${projects.folder-path}")
    private String pathToProjectsFolder;

    @Transactional
    public LasFileUploadResponse upload(MultipartFile file, Long projectId) throws IOException {
        try {
            ProjectEntity project = projectRepository.findById(projectId).orElseThrow(()
                -> new NoSuchElementException("Проект с id " + projectId + " не существует"));
            File tempFile = fileUtil.createTempFile(file);
            LasReader lasReader = new LasReader(tempFile.getAbsolutePath());
            lasReader.read();
            tempFile.delete();
            File projectFolder = fileUtil.createProject(projectId);
            saveCurves(lasReader.getCurves(),
                    projectFolder.getAbsolutePath() + "\\data",
                    project);
            projectRepository.save(project);
            return LasFileUploadResponse.builder()
                    .curvesNames(getCurvesNames(project.getCurves()))
                    .build();
        } catch (IOException | IllegalStateException i) {
            throw new IOException("Can't read file");
        } catch (NoSuchElementException o) {
            throw new NoSuchElementException("Проект с id " + projectId + " не существует");
        } catch (RuntimeException v) {
            throw new RuntimeException(v.getMessage());
        }
    }

    public CurveDataDownloadResponse download(Long projectId, String curveName) {
        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(()
                -> new NoSuchElementException("Проект c id " + projectId + " не существует"));
        List<CurveEntity> curves = project.getCurves();
        if (curves.isEmpty()) {
            throw new NoSuchElementException("Информация о Кривой " + curveName + " не найдена");
        }
        String curveDataInJson = fileUtil.getCurveData(findCurveByName(curves, curveName).getDataFile());
        return CurveDataDownloadResponse.builder()
                .curveDataInJson(curveDataInJson)
                .build();
    }

    public GetCurvesNamesResponse getCurvesNames(Long projectId) {
        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(()
                -> new NoSuchElementException("Проект c id " + projectId + " не существует"));
        return GetCurvesNamesResponse.builder()
                .curvesNames(getCurvesNames(project.getCurves()))
                .build();
    }



    private List<String> getCurvesNames(List<CurveEntity> curves) {
        List<String> curvesNames = new ArrayList<>();
        for (CurveEntity curve : curves) {
            curvesNames.add(curve.getName());
        }
        return curvesNames;
    }
    private void saveCurves(List<Curve> curves, String folderPath, ProjectEntity project) throws IOException {
        Gson gson = new Gson();
        List<CurveEntity> projectCurves = project.getCurves();
        for (Curve curve : curves) {
            File curveData = new File(folderPath + "\\" + curve.getName());
            curveData.createNewFile();
            CurveEntity curveEntity = CurveEntity.builder()
                    .dataFile(curveData)
                    .name(curve.getName())
                    .project(project)
                    .build();
            projectCurves.add(curveEntity);
            try (FileWriter writer = new FileWriter(curveData)) {
                writer.write(gson.toJson(curve.getData()));
            }
        }
    }

    private CurveEntity findCurveByName(List<CurveEntity> curves, String curveName) {
        for (CurveEntity curve : curves) {
            if (curve.getName().equals(curveName)) {
                return curve;
            }
        }
        throw new NoSuchElementException("Кривая " + curveName + " не существует");
    }
}

