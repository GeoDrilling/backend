package ru.nsu.fit.geodrilling.controllers;

import java.io.IOException;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.fit.geodrilling.services.file.LasFileService;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class LasFileController {

    private final LasFileService lasFileService;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok((lasFileService.upload(file)));
    }


}
