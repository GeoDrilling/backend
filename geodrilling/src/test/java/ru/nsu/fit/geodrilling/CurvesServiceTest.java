package ru.nsu.fit.geodrilling;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.web.MockMultipartFile;
import ru.nsu.fit.geodrilling.entity.ProjectEntity;
import ru.nsu.fit.geodrilling.repositories.ProjectRepository;
import ru.nsu.fit.geodrilling.services.CurvesService;
import ru.nsu.fit.geodrilling.services.FileService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
public class CurvesServiceTest {

    @SpyBean
    private ProjectRepository projectRepository;

    @SpyBean
    private FileService fileService;

    @SpyBean
    private CurvesService curvesService;

    @Autowired
    private Gson gson;

    @BeforeEach
    public void mockState() throws IOException {
        doNothing().when(curvesService).updateDeptInProjectState(anyLong());
        doNothing().when(curvesService).updateTvdInProjectState(anyLong(), anyString());
    }

    @Test
    public void saveCsvTest() throws IOException {
        MockMultipartFile multipartFile = new MockMultipartFile("M5341", "M5341.csv", "text/csv" , Files.readAllBytes(Path.of("src/test/resources/M_5341.csv")));
        ProjectEntity project = new ProjectEntity();
        Mockito.when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));
        fileService.save(multipartFile, 1L);
        Assertions.assertEquals(20, project.getCurves().size());
        Assertions.assertEquals(5303.87, gson.fromJson(project.getCurves().get(0).getData(),
                new TypeToken<List<Double>>(){}).get(0));
        Assertions.assertEquals(5333.13, gson.fromJson(project.getCurves().get(0).getData(),
                new TypeToken<List<Double>>(){}).get(1));
    }

//    @Test
//    public void supplementTest() throws IOException {
//        ProjectEntity project = new ProjectEntity();
//        Mockito.when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));
//        MockMultipartFile initDataFile = new MockMultipartFile("M5341", "M5341.csv", "text/csv" , Files.readAllBytes(Path.of("src/test/resources/M_5341.csv")));
//        curvesService.save(initDataFile, 1L);
//        MockMultipartFile supplementingDataFile = new MockMultipartFile("M5457", "M5457.csv", "text/csv" , Files.readAllBytes(Path.of("src/test/resources/M_5457.csv")));
//        curvesService.supplementCurve(supplementingDataFile, 1L);
//        Assertions.assertEquals();
//
//    }

    @Test
    public void handleMultiCurvesTest() throws IOException {
        MockMultipartFile multipartFile = new MockMultipartFile("data", "data.las", "application/octet-stream" , Files.readAllBytes(Path.of("src/test/resources/data.las")));
        ProjectEntity project = new ProjectEntity();
        Mockito.when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));
        fileService.save(multipartFile, 1L);
    }
}
