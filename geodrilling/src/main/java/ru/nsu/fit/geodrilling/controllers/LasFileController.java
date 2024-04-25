package ru.nsu.fit.geodrilling.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.fit.geodrilling.dto.curves.CurveDataDownloadResponse;
import ru.nsu.fit.geodrilling.dto.curves.CurveSupplementationResponse;
import ru.nsu.fit.geodrilling.dto.curves.GetCurvesNamesResponse;
import ru.nsu.fit.geodrilling.dto.curves.SaveCurveDataResponse;
import ru.nsu.fit.geodrilling.services.CurvesService;
import ru.nsu.fit.geodrilling.services.SootService;

import java.io.IOException;

@RestController
@RequestMapping("/lasfile")
@RequiredArgsConstructor
public class LasFileController {

    private final CurvesService curvesService;
    private final SootService sootService;
    @PostMapping("/upload")
    public ResponseEntity<SaveCurveDataResponse> upload(@RequestParam("file") MultipartFile file,
                                                        @RequestParam("project_id") Long projectId) throws IOException {
        SaveCurveDataResponse saveCurveDataResponse = (curvesService.save(file, projectId));
        sootService.sootOffer(saveCurveDataResponse, projectId);
        return ResponseEntity.ok(saveCurveDataResponse);
    }

    @GetMapping("/download/curve")
    public ResponseEntity<CurveDataDownloadResponse> downloadByCurveName(
            @RequestParam("project_id") Long projectId,
            @RequestParam("curve_name") String curveName) {
        return ResponseEntity.ok(curvesService.getCurveDataByName(curveName, projectId, false));
    }

    @GetMapping("/download/curve/synthetic")
    public ResponseEntity<CurveDataDownloadResponse> downloadSyntheticByCurveName(
            @RequestParam("project_id") Long projectId,
            @RequestParam("curve_name") String curveName) {
        return ResponseEntity.ok(curvesService.getCurveDataByName(curveName, projectId, true));
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

    @GetMapping("/dept/max")
    public ResponseEntity<Double> getDeptMax(@RequestParam("project_id") Long projectId) {
        return ResponseEntity.ok(curvesService.getDeptMax(projectId));
    }

    @GetMapping("/dept/min")
    public ResponseEntity<Double> getDeptMin(@RequestParam("project_id") Long projectId) {
        return ResponseEntity.ok(curvesService.getDeptMin(projectId));
    }
}
