package ru.nsu.fit.geodrilling.services.file;

import com.google.gson.Gson;
import grillid9.laslib.Curve;
import grillid9.laslib.LasReader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import grillid9.laslib.exceptions.VersionException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.fit.geodrilling.dto.CurveDataDownloadResponse;
import ru.nsu.fit.geodrilling.dto.LasFileUploadResponse;
import ru.nsu.fit.geodrilling.entity.LasEntity;
import ru.nsu.fit.geodrilling.entity.ProjectEntity;
import ru.nsu.fit.geodrilling.repositories.ProjectRepository;

@Service
@RequiredArgsConstructor
public class LasFileService {

    private final ProjectRepository projectRepository;

    @Value("${lasfile.temp-path}")
    private String pathToTempFolder;

    @Value("${projects.folder-path}")
    private String pathToProjectsFolder;

    @Transactional
    public LasFileUploadResponse upload(MultipartFile file, Long projectId) throws IOException {
        try {
            ProjectEntity project = projectRepository.findById(projectId).orElseThrow();
            File tempFile = new File(pathToTempFolder + file.getOriginalFilename());
            file.transferTo(tempFile);
            LasReader lasReader = new LasReader(tempFile.getAbsolutePath());
            lasReader.read();
            tempFile.delete();
            File folder = new File(pathToProjectsFolder + "\\project" + project.getId()
                    + "\\" + file.getOriginalFilename());
            folder.mkdirs();
            saveCurvesDataAsJsonFiles(lasReader.getCurves(), folder.getAbsolutePath());
            Gson gson = new Gson();
            Set<String> curvesNames = getCurvesNames(lasReader.getCurves());
            LasEntity lasEntity = LasEntity.builder()
                    .name(file.getOriginalFilename())
                    .curvesNames(curvesNames)
                    .curvesNamesInJson(gson.toJson(curvesNames))
                    .project(project)
                    .build();
            project.getLas().put(lasEntity.getName(), lasEntity);
            projectRepository.save(project);
            return LasFileUploadResponse.builder()
                    .curvesNames(lasEntity.getCurvesNamesInJson())
                    .build();
        } catch (IOException | IllegalStateException i) {
            throw new IOException("Can't read file");
        } catch (NoSuchElementException o) {
            throw new NoSuchElementException("Проект с id " + projectId + " не существует");
        } catch (VersionException v) {
            throw new VersionException(v.getMessage());
        }
    }

    public CurveDataDownloadResponse download(Long projectId, String fileName, String curveName) {
        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(()
                -> new NoSuchElementException("Проект c id " + projectId + " не существует"));
        Map<String, LasEntity> lasEntities = project.getLas();
        LasEntity lasFile = lasEntities.get(fileName);
        if (lasFile == null) {
            throw new NoSuchElementException("Файл " + fileName + " не найден");
        }
        if (!lasFile.getCurvesNames().contains(curveName)) {
            throw new NoSuchElementException("Информация о Кривой " + curveName + " не найдена");
        }
        File curveFile = new File(pathToProjectsFolder + "\\project" + projectId
                + "\\" + fileName + "\\" + curveName);
        String curveDataInJson;
        try {
            curveDataInJson = new String(Files.readAllBytes(Paths.get(curveFile.getAbsolutePath())));
        } catch (IOException e) {
            throw new RuntimeException("Невозможно считать данные кривой");
        }
        return CurveDataDownloadResponse.builder()
                .curveDataInJson(curveDataInJson)
                .build();
    }

    private Set<String> getCurvesNames(List<Curve> curves) {
        Set<String> curvesNames = new HashSet<>();
        for (Curve curve : curves) {
            curvesNames.add(curve.getName());
        }
        return curvesNames;
    }

    private void saveCurvesDataAsJsonFiles(List<Curve> curves, String folderPath) throws IOException {
        Gson gson = new Gson();
        for (Curve curve : curves) {
            File curveData = new File(folderPath + "\\" + curve.getName());
            curveData.createNewFile();
            try (FileWriter writer = new FileWriter(curveData)) {
                writer.write(gson.toJson(curve.getData()));
            }
        }
    }
}

