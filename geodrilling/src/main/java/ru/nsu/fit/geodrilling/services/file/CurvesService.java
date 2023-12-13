package ru.nsu.fit.geodrilling.services.file;

import com.google.gson.Gson;
import grillid9.laslib.Curve;
import grillid9.laslib.LasReader;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.fit.geodrilling.dto.curves.LasFileUploadResponse;
import ru.nsu.fit.geodrilling.entity.CurveEntity;
import ru.nsu.fit.geodrilling.entity.ProjectEntity;
import ru.nsu.fit.geodrilling.exceptions.NewCurvesAddingException;
import ru.nsu.fit.geodrilling.repositories.ProjectRepository;
import ru.nsu.fit.geodrilling.utils.FileUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CurvesService {

    private final ProjectRepository projectRepository;
    private final FileUtil fileUtil;
    private Gson gson = new Gson();

    @Value("${projects.folder-path}")
    String projectsFolderPath;

    @Transactional
    public LasFileUploadResponse addNewCurves(MultipartFile file, Long projectId) throws IOException {
        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(()
                -> new NoSuchElementException("Проект с id " + projectId + " не существует"));
        File tempFile = fileUtil.createTempFile(file);
        LasReader lasReader = new LasReader(tempFile.getAbsolutePath());
        lasReader.read();
        tempFile.delete();
        CurveEntity depthInProjectCurve = findCurveEntityByName("DEPT", project.getCurves());
        Curve newDepthCurve = findCurveByName("DEPT", lasReader.getCurves());
        String depthInProjectData = fileUtil.getCurveData(depthInProjectCurve.getDataFile());
        if (!depthInProjectData.equals(gson.toJson(newDepthCurve.getData()))) {
            throw new RuntimeException("Кривая DEPT не соответствует уже добалвенной кривой");
        }
        lasReader.getCurves().remove(newDepthCurve);
        File projectDataFolderPath = new File(projectsFolderPath + "\\project" + projectId + "\\data");
        saveCurves(lasReader.getCurves(), project, projectDataFolderPath);
        return LasFileUploadResponse.builder()
                .curvesNames(getCurvesNames(project.getCurves()))
                .build();
    }

    @Transactional
    public LasFileUploadResponse dataSupplementation(MultipartFile file, Long projectId) throws IOException {
        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(()
            -> new NoSuchElementException("Проект с id " + projectId + " не найден"));
        if (project.getCurves().isEmpty()) {
            throw new NoSuchElementException("Кривые в проекте не найдены");
        }
        File tempFile = fileUtil.createTempFile(file);
        LasReader lasReader = new LasReader(tempFile.getAbsolutePath());
        lasReader.read();
        tempFile.delete();
        return null; // TODO
    }



    private void saveCurves(List<Curve> curves, ProjectEntity project, File dir) throws IOException {
        Gson gson = new Gson();
        List<CurveEntity> curvesEntities = project.getCurves();
        List<String> curvesNames;
        for (Curve curve : curves) {
            File dataFile = new File(dir + "\\" + curve.getName());
            if (dataFile.exists()) continue;
            dataFile.createNewFile();
            try (FileWriter writer = new FileWriter(dataFile)) {
                writer.write(gson.toJson(curve.getData()));
            }
            curvesEntities.add(CurveEntity.builder()
                            .name(curve.getName())
                            .dataFile(dataFile)
                            .project(project)
                            .build());
        }
    }

    private CurveEntity findCurveEntityByName(String curveName, List<CurveEntity> curves) {
        for (CurveEntity curve : curves) {
            if (curve.getName().equals(curveName)) {
                return curve;
            }
        }
        throw new NoSuchElementException("Кривой " + curveName
                + " не существует");
    }

    private Curve findCurveByName(String curveName, List<Curve> curves) {
        for (Curve curve : curves) {
            if (curve.getName().equals(curveName)) {
                return curve;
            }
        }
        throw new NoSuchElementException("Кривой " + curveName
                + " не существует");
    }
    private List<String> getCurvesEntitiesNames(List<CurveEntity> curves) {
        List<String> curvesNames = new ArrayList<>();
        for (CurveEntity curve : curves) {
            curvesNames.add(curve.getName());
        }
        return curvesNames;
    }

    private List<String> getCurvesNames(List<CurveEntity> curves) {
        List<String> curvesNames = new ArrayList<>();
        for (CurveEntity curve : curves) {
            curvesNames.add(curve.getName());
        }
        return curvesNames;
    }

}


