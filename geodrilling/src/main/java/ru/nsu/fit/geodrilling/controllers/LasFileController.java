package ru.nsu.fit.geodrilling.controllers;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.fit.geodrilling.dto.CurveDataDownloadResponse;
import ru.nsu.fit.geodrilling.dto.LasFileUploadResponse;
import ru.nsu.fit.geodrilling.services.file.LasFileService;

@RestController
@RequestMapping("/lasfile")
@RequiredArgsConstructor
public class LasFileController {

    private final LasFileService lasFileService;

    @PostMapping("/upload")
    public ResponseEntity<LasFileUploadResponse> upload(@RequestParam("file") MultipartFile file, @RequestParam("project_id") Long projectId) throws IOException {
        return ResponseEntity.ok((lasFileService.upload(file, projectId)));
    }

    @GetMapping("/download/curve")
    public ResponseEntity<CurveDataDownloadResponse> downloadByCurveName(
            @RequestParam("project_id") Long projectId,
            @RequestParam("file_name") String fileName,
            @RequestParam("curve_name") String curveName) {
        return ResponseEntity.ok((lasFileService.download(projectId, fileName, curveName)));
    }
}
