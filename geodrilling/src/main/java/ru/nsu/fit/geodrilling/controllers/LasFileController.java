package ru.nsu.fit.geodrilling.controllers;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.fit.geodrilling.dto.curves.*;
import ru.nsu.fit.geodrilling.services.CurvesService;
import ru.nsu.fit.geodrilling.dto.curves.CurveDataDownloadResponse;
import ru.nsu.fit.geodrilling.dto.curves.GetCurvesNamesResponse;
import ru.nsu.fit.geodrilling.dto.curves.LasFileUploadResponse;
import ru.nsu.fit.geodrilling.services.SootService;
import ru.nsu.fit.geodrilling.services.file.CurvesService;
import ru.nsu.fit.geodrilling.services.file.LasFileService;

@RestController
@RequestMapping("/lasfile")
@RequiredArgsConstructor
public class LasFileController {

    private final LasFileService lasFileService;
    private final CurvesService curvesService;
    private final SootService sootService;
    @PostMapping("/upload")
    public ResponseEntity<SaveCurveDataResponse> upload(@RequestParam("file") MultipartFile file,
                                                        @RequestParam("project_id") Long projectId) throws IOException {
        return ResponseEntity.ok((curvesService.saveCurves(file, projectId)));
    }

    @GetMapping("/download/curve")
    public ResponseEntity<CurveDataDownloadResponse> downloadByCurveName(
            @RequestParam("project_id") Long projectId,
            @RequestParam("curve_name") String curveName) {
        return ResponseEntity.ok(curvesService.getCurveDataByName(curveName, projectId));
    }

    @GetMapping("/curves")
    public ResponseEntity<GetCurvesNamesResponse> getCurvesNames(
            @RequestParam("project_id") Long projectId) {
        return ResponseEntity.ok(curvesService.getCurvesNames(projectId));
    }

    @PostMapping("/upload/supplement")
    public ResponseEntity<CurveSupplementationResponse> dataSupplementation(
            @RequestParam("file") MultipartFile file,
            @RequestParam("project_id") Long projectId) throws IOException {
        return ResponseEntity.ok(curvesService.supplementCurve(file, projectId));
    }
}
