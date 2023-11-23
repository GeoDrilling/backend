package ru.nsu.fit.geodrilling.services.file;

import com.google.gson.Gson;
import grillid9.laslib.Curve;
import grillid9.laslib.LasReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import grillid9.laslib.exceptions.VersionException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.fit.geodrilling.dto.LasFileUploadResponse;
import ru.nsu.fit.geodrilling.entity.LasEntity;
import ru.nsu.fit.geodrilling.entity.ProjectEntity;
import ru.nsu.fit.geodrilling.repositories.ProjectRepository;

@Service
@RequiredArgsConstructor
public class LasFileService {

    private final ProjectRepository projectRepository;

    @Transactional
    public LasFileUploadResponse upload(MultipartFile file, Long projectId) throws IOException {
        String curvesNames;
        try {
            ProjectEntity project = projectRepository.findById(projectId).orElseThrow();
            File fileDest = new File("C:\\GeoDrillingData\\temp\\" + file.getOriginalFilename());
            file.transferTo(fileDest);
            LasReader lasReader = new LasReader(fileDest.getAbsolutePath());
            lasReader.read();
            File folder = new File("C:\\GeoDrillingData\\project" + project.getId()
                    + "\\" + file.getOriginalFilename());
            folder.mkdirs();
            saveCurvesDataAsJsonFiles(lasReader.getCurves(), folder.getAbsolutePath());
            LasEntity lasEntity = new LasEntity();
            curvesNames = getCurvesNamesAsJson(lasReader.getCurves());
            lasEntity.setCurvesNames(curvesNames);
            project.getLas().add(lasEntity);
            projectRepository.save(project);
        } catch (IOException | IllegalStateException i) {
            throw new IOException("Can't read file");
        } catch (NoSuchElementException o) {
            throw new NoSuchElementException("There is no project with id: " + projectId);
        } catch (VersionException v) {
            throw new VersionException(v.getMessage());
        }
        return LasFileUploadResponse.builder()
                .curvesNames(curvesNames)
                .build();
    }

    public String getCurvesNamesAsJson(List<Curve> curves) {
        Gson gson = new Gson();
        List<String> curvesNames = new ArrayList<>();
        for (Curve curve : curves) {
            curvesNames.add(curve.getName());
        }
        return gson.toJson(curvesNames);
    }

    public void saveCurvesDataAsJsonFiles(List<Curve> curves, String folderPath) throws IOException {
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

