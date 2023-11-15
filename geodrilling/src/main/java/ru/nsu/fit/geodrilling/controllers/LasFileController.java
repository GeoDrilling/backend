package ru.nsu.fit.geodrilling.controllers;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
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


}
