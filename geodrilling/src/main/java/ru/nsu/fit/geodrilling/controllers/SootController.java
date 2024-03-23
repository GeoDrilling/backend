package ru.nsu.fit.geodrilling.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.geodrilling.dto.SootinDTO;
import ru.nsu.fit.geodrilling.dto.SootoutDTO;
import ru.nsu.fit.geodrilling.entity.SootEntity;
import ru.nsu.fit.geodrilling.services.SootService;

@RestController
@RequestMapping("/soot")
@AllArgsConstructor
public class SootController {
    private final SootService sootService;

    @PostMapping("/rename/{project_id}")
    public ResponseEntity<String> sootRename(
            @PathVariable("project_id") Long idProject,
            @RequestBody SootinDTO sootinDTO) throws Exception {
        return ResponseEntity.ok(sootService.sootRename(idProject, sootinDTO));
    }
    @PostMapping("/out")
    public ResponseEntity<SootoutDTO> sootOut(
            @RequestParam("project_id") Long idProject) throws Exception {
        return ResponseEntity.ok(sootService.sootOut(idProject));
    }

    @GetMapping("/checkCurves")
    public Boolean checkCurves(@RequestParam("project_id") Long projectId){
        return sootService.checkCurves(projectId);
    }
}
