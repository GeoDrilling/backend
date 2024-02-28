package ru.nsu.fit.geodrilling.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.fit.geodrilling.dto.InputParamAreasDTO;
import ru.nsu.fit.geodrilling.model.AreasEquivalence;
import ru.nsu.fit.geodrilling.services.AreasService;
import ru.nsu.fit.geodrilling.services.ModelService;

@RestController
@RequestMapping("/areas")
@AllArgsConstructor
public class AreasController {
    private final AreasService areasService;
    @PostMapping("/create")
    public ResponseEntity<AreasEquivalence> createAreas(
            @RequestParam("model_id") Long idModel,
            @RequestParam("name") InputParamAreasDTO idParamDTO) throws Exception {
     /*   UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String email = (modelMapper.map(( (UserDetails) token.getPrincipal()), UserDTO.class).getEmail());*/
        return ResponseEntity.ok(areasService.createAreas(idModel, idParamDTO));
    }
}
