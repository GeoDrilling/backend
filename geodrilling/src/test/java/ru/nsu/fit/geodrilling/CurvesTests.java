package ru.nsu.fit.geodrilling;

import com.google.gson.Gson;
import grillid9.laslib.LasReader;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import ru.nsu.fit.geodrilling.dto.AuthenticationResponse;
import ru.nsu.fit.geodrilling.dto.ProjectStateDTO;
import ru.nsu.fit.geodrilling.dto.RegisterRequest;
import ru.nsu.fit.geodrilling.repositories.ProjectRepository;
import ru.nsu.fit.geodrilling.services.CurvesService;
import ru.nsu.fit.geodrilling.services.ProjectService;
import ru.nsu.fit.geodrilling.services.UserService;
import ru.nsu.fit.geodrilling.services.auth.AuthenticationService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class CurvesTests {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private CurvesService curvesService;

    @Autowired
    private ProjectRepository projectRepository;

//    @Test
//    public void saveSyntheticCurveTest() throws IOException {
//        AuthenticationResponse authentication = authenticationService.register(RegisterRequest.builder()
//            .email("m.maksimenkov@g.nsu.ru")
//            .name("Maxim777")
//            .password("123456v")
//            .build(), new MockHttpServletResponse());
//        ProjectStateDTO projectDTO = projectService.createProjectForUser(authentication.getUser().getEmail(), "Project1");
//        LasReader lasReader = new LasReader("src\\test\\resources\\test.las");
//        lasReader.read();
//        List<Double> data = lasReader.getCurves().get(0).getData()
//                .stream().map(Double::valueOf).collect(Collectors.toList());
//        curvesService.saveSyntheticCurve(projectRepository.findById(projectDTO.getId()).get(), "TEST", data);
//        Gson gson = new Gson();
//        Assertions.assertEquals(gson.toJson(data), Files.readString(Path.of(projectsFolderPath +
//                "\\project" + projectDTO.getId() + "\\data\\synthetic\\TEST")));
//    }
}
