package ru.nsu.fit.geodrilling.controllers;

import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.geodrilling.dto.InputParamAreasDTO;
import ru.nsu.fit.geodrilling.dto.SootinDTO;
import ru.nsu.fit.geodrilling.model.AreasEquivalence;
import ru.nsu.fit.geodrilling.services.AreasService;
import ru.nsu.fit.geodrilling.services.ModelService;

@RestController
@RequestMapping("/areas")
@AllArgsConstructor
public class AreasController {
    private final AreasService areasService;
    @PostMapping("/create/{model_id}")
    public ResponseEntity<ByteArrayResource> createAreas(
            @PathVariable("model_id") Long idModel,
            @RequestBody InputParamAreasDTO inputParamAreasDTO) throws Exception {
     /*   UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String email = (modelMapper.map(( (UserDetails) token.getPrincipal()), UserDTO.class).getEmail());*/
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(areasService.createAreas(idModel, inputParamAreasDTO));
    }


}
