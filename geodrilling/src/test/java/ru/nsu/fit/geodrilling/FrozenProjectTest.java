package ru.nsu.fit.geodrilling;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import ru.nsu.fit.geodrilling.entity.ProjectEntity;
import ru.nsu.fit.geodrilling.exceptions.FrozenProjectException;
import ru.nsu.fit.geodrilling.repositories.ProjectRepository;
import ru.nsu.fit.geodrilling.services.CurvesService;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
public class FrozenProjectTest {

    @MockBean
    public ProjectRepository mockedProjectRepository;

    @Autowired
    public CurvesService curvesService;

    @BeforeEach
    public void initTestProject() {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setReadOnly(true);
        when(mockedProjectRepository.findById(anyLong())).thenReturn(Optional.of(projectEntity));
        when(mockedProjectRepository.save(any())).thenReturn(null);
    }

    @Test
    public void addCurvesToFrozenProjectTest() throws IOException {
        Assertions.assertThrows(FrozenProjectException.class,
                () -> curvesService.saveCurves(new MockMultipartFile("test", new byte[]{}), 1L));
    }

    @Test
    public void supplementCurvesToFrozenProjectTest() {
        Assertions.assertThrows(FrozenProjectException.class,
                () -> curvesService.supplementCurve(new MockMultipartFile("test", new byte[]{}), 1L));
    }

}
