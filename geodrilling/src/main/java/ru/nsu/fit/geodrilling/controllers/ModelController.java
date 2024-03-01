package ru.nsu.fit.geodrilling.controllers;

import lombok.AllArgsConstructor;
import org.hibernate.result.Output;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.geodrilling.dto.InputBuildModel;
import ru.nsu.fit.geodrilling.dto.ModelDTO;
import ru.nsu.fit.geodrilling.dto.UserDTO;
import ru.nsu.fit.geodrilling.entity.ProjectEntity;
import ru.nsu.fit.geodrilling.model.OutputModel;
import ru.nsu.fit.geodrilling.services.ModelService;
import ru.nsu.fit.geodrilling.services.UserService;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/model")
@AllArgsConstructor
public class ModelController {
    private final UserService userService;
    private final ModelService modelService;
    private final ModelMapper modelMapper;

    @PostMapping("/create")
    public ResponseEntity<ModelDTO> createModel(
            @RequestParam("project_id") Long idProject,
            @RequestParam("name") String name) throws Exception {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String email = (modelMapper.map(( (UserDetails) token.getPrincipal()), UserDTO.class).getEmail());
        return ResponseEntity.ok(modelService.createModel(idProject, name, email));
    }
}
