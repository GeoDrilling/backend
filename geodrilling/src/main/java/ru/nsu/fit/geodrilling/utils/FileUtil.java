package ru.nsu.fit.geodrilling.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class FileUtil {

    @Value("${lasfile.temp-path}")
    private String tempFolderPath;

    @Value("${projects.folder-path}")
    private String projectsFolderPath;

    public File createProject(Long projectId) {
        File projectFolder = new File(projectsFolderPath + "\\project" + projectId);
        if (!projectFolder.exists()) {
            projectFolder.mkdir();
            File dataFolder = new File(projectFolder.getAbsolutePath() + "\\data");
            dataFolder.mkdir();
        }
        return projectFolder;
    }

    public File createTempFile(MultipartFile file) throws IOException {
        File tempFolder = new File(tempFolderPath);
        if (!tempFolder.exists()) {
            tempFolder.mkdir();
        }
        File tempFile = new File(tempFolderPath + "\\" + file.getOriginalFilename());
        file.transferTo(tempFile);
        return tempFile;
    }

    public String getCurveData(File dataFile) {
        String curveDataInJson;
        try {
            curveDataInJson = new String(Files.readAllBytes(Paths.get(dataFile.getAbsolutePath())));
        } catch (IOException e) {
            throw new RuntimeException("Невозможно считать данные кривой");
        }
        return curveDataInJson;
    }
}
