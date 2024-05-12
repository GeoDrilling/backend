package ru.nsu.fit.geodrilling;


import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.nsu.fit.geodrilling.dto.ModelDTO;
import ru.nsu.fit.geodrilling.dto.SaveModelResponse;
import ru.nsu.fit.geodrilling.repositories.ProjectRepository;
import ru.nsu.fit.geodrilling.repositories.UserRepository;
import ru.nsu.fit.geodrilling.services.ModelService;
import ru.nsu.fit.geodrilling.services.ProjectService;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

@SpringBootTest
public class ModelServiceTest {
    @Autowired
    ModelService modelService;

    @Autowired
    ProjectService projectService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    public ModelDTO fillModelDTO(double value) {
        ModelDTO modelDTO = new ModelDTO();
        modelDTO.setAlpha(value);
        modelDTO.setKanisotropyDown(value);
        modelDTO.setKanisotropyUp(value);
        modelDTO.setRoDown(value);
        modelDTO.setRoUp(value);
        modelDTO.setTvdStart(value);
        return modelDTO;
    }

    public boolean checkModelDTOFields(ModelDTO modelDTO, double value) {
        return modelDTO.getAlpha() == value &&
                modelDTO.getKanisotropyDown() == value &&
                modelDTO.getKanisotropyUp() == value &&
                modelDTO.getRoDown() == value &&
                modelDTO.getRoUp() == value &&
                modelDTO.getTvdStart() == value;
    }



    @Test
    void test() {
        Long userId = userRepository.findByEmail("test@mail.ru").
                orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")).getId();
        Long projectId = projectRepository.findAllByUserId(userId).get(0).getId();
        ModelDTO modelDTO = fillModelDTO(1.0D);
        modelDTO.setStart(3163.8);
        modelDTO.setEnd(3728.6);
        SaveModelResponse saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        List<ModelDTO> modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 1, "size!=1");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3728.6, "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.0), "1");
    }

    @Test
    void test2() {
        Long userId = userRepository.findByEmail("test@mail.ru").
                orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")).getId();
        Long projectId = projectRepository.findAllByUserId(userId).get(0).getId();
        ModelDTO modelDTO = fillModelDTO(1.0D);
        modelDTO.setStart(3163.8);
        modelDTO.setEnd(3728.6);
        SaveModelResponse saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        List<ModelDTO> modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 1, "size!=1");

        modelDTO = fillModelDTO(1.1D);
        modelDTO.setStart(3263.8);
        modelDTO.setEnd(3628.6);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 3, "size!=3");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3263.8, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3263.8 && modelDTOList.get(1).getEnd() == 3628.6, "2");
        assertTrue(modelDTOList.get(2).getStart() == 3628.6 && modelDTOList.get(2).getEnd() == 3728.6, "3");

        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.0), "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(1), 1.1), "2");
        assertTrue(checkModelDTOFields(modelDTOList.get(2), 1.0), "3");

    }


    @Test
    void test3() {
        Long userId = userRepository.findByEmail("test@mail.ru").
                orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")).getId();
        Long projectId = projectRepository.findAllByUserId(userId).get(0).getId();
        ModelDTO modelDTO = fillModelDTO(1.0D);
        modelDTO.setStart(3163.8);
        modelDTO.setEnd(3728.6);
        SaveModelResponse saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        List<ModelDTO> modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 1, "size!=1");

        modelDTO = fillModelDTO(1.1D);
        modelDTO.setStart(3163.8);
        modelDTO.setEnd(3328.6);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 2, "size!=3");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3328.6, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3328.6 && modelDTOList.get(1).getEnd() == 3728.6, "2");

        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.1), "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(1), 1.0), "2");

    }

    @Test
    void test4() {
        Long userId = userRepository.findByEmail("test@mail.ru").
                orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")).getId();
        Long projectId = projectRepository.findAllByUserId(userId).get(0).getId();
        ModelDTO modelDTO = fillModelDTO(1.0D);
        modelDTO.setStart(3163.8);
        modelDTO.setEnd(3728.6);
        SaveModelResponse saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        List<ModelDTO> modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 1, "size!=1");

        modelDTO = fillModelDTO(1.1D);
        modelDTO.setStart(3363.8);
        modelDTO.setEnd(3728.6);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 2, "size!=3");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3363.8, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3363.8 && modelDTOList.get(1).getEnd() == 3728.6, "2");

        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.0), "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(1), 1.1), "2");
    }


    @Test
    void testRaz() {
        Long userId = userRepository.findByEmail("test@mail.ru").
                orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")).getId();
        Long projectId = projectRepository.findAllByUserId(userId).get(0).getId();
        ModelDTO modelDTO = fillModelDTO(1.0D);
        modelDTO.setStart(3163.8);
        modelDTO.setEnd(3728.6);
        SaveModelResponse saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        List<ModelDTO> modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 1, "size!=1");

        modelDTO = fillModelDTO(1.1D);
        modelDTO.setStart(3263.8);
        modelDTO.setEnd(3628.6);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 3, "size!=3");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3263.8, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3263.8 && modelDTOList.get(1).getEnd() == 3628.6, "2");
        assertTrue(modelDTOList.get(2).getStart() == 3628.6 && modelDTOList.get(2).getEnd() == 3728.6, "3");

        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.0), "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(1), 1.1), "2");
        assertTrue(checkModelDTOFields(modelDTOList.get(2), 1.0), "3");

        modelDTO = fillModelDTO(1.2D);
        modelDTO.setStart(3253.8);
        modelDTO.setEnd(3618.6);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 4, "size!=4");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3253.8, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3253.8 && modelDTOList.get(1).getEnd() == 3618.6, "2");
        assertTrue(modelDTOList.get(2).getStart() == 3618.6 && modelDTOList.get(2).getEnd() == 3628.6, "3");
        assertTrue(modelDTOList.get(3).getStart() == 3628.6 && modelDTOList.get(3).getEnd() == 3728.6, "4");

        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.0), "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(1), 1.2), "2");
        assertTrue(checkModelDTOFields(modelDTOList.get(2), 1.1), "3");
        assertTrue(checkModelDTOFields(modelDTOList.get(3), 1.0), "4");

    }

    @Test
    void testR() {
        Long userId = userRepository.findByEmail("test@mail.ru").
                orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")).getId();
        Long projectId = projectRepository.findAllByUserId(userId).get(0).getId();
        ModelDTO modelDTO = fillModelDTO(1.0D);
        modelDTO.setStart(3163.8);
        modelDTO.setEnd(3728.6);
        SaveModelResponse saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        List<ModelDTO> modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 1, "size!=1");

        modelDTO = fillModelDTO(1.1D);
        modelDTO.setStart(3263.8);
        modelDTO.setEnd(3628.6);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 3, "size!=3");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3263.8, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3263.8 && modelDTOList.get(1).getEnd() == 3628.6, "2");
        assertTrue(modelDTOList.get(2).getStart() == 3628.6 && modelDTOList.get(2).getEnd() == 3728.6, "3");

        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.0), "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(1), 1.1), "2");
        assertTrue(checkModelDTOFields(modelDTOList.get(2), 1.0), "3");

        modelDTO = fillModelDTO(1.2D);
        modelDTO.setStart(3273.8);
        modelDTO.setEnd(3698.6);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 4, "size!=4");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3263.8, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3263.8 && modelDTOList.get(1).getEnd() == 3273.8, "2");
        assertTrue(modelDTOList.get(2).getStart() == 3273.8 && modelDTOList.get(2).getEnd() == 3698.6, "3");
        assertTrue(modelDTOList.get(3).getStart() == 3698.6 && modelDTOList.get(3).getEnd() == 3728.6, "4");

        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.0), "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(1), 1.1), "2");
        assertTrue(checkModelDTOFields(modelDTOList.get(2), 1.2), "3");
        assertTrue(checkModelDTOFields(modelDTOList.get(3), 1.0), "4");

    }


    @Test
    void testL() {
        Long userId = userRepository.findByEmail("test@mail.ru").
                orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")).getId();
        Long projectId = projectRepository.findAllByUserId(userId).get(0).getId();
        ModelDTO modelDTO = fillModelDTO(1.0D);
        modelDTO.setStart(3163.8);
        modelDTO.setEnd(3728.6);
        SaveModelResponse saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        List<ModelDTO> modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 1, "size!=1");

        modelDTO = fillModelDTO(1.1D);
        modelDTO.setStart(3263.8);
        modelDTO.setEnd(3628.6);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 3, "size!=3");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3263.8, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3263.8 && modelDTOList.get(1).getEnd() == 3628.6, "2");
        assertTrue(modelDTOList.get(2).getStart() == 3628.6 && modelDTOList.get(2).getEnd() == 3728.6, "3");

        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.0), "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(1), 1.1), "2");
        assertTrue(checkModelDTOFields(modelDTOList.get(2), 1.0), "3");

        modelDTO = fillModelDTO(1.2D);
        modelDTO.setStart(3253.8);
        modelDTO.setEnd(3698.6);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 3, "size!=3");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3253.8, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3253.8 && modelDTOList.get(1).getEnd() == 3698.6, "2");
        assertTrue(modelDTOList.get(2).getStart() == 3698.6 && modelDTOList.get(2).getEnd() == 3728.6, "3");

        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.0), "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(1), 1.2), "2");
        assertTrue(checkModelDTOFields(modelDTOList.get(2), 1.0), "3");
    }

    @Test
    void testRaz2AndD() {
        Long userId = userRepository.findByEmail("test@mail.ru").
                orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")).getId();
        Long projectId = projectRepository.findAllByUserId(userId).get(0).getId();
        ModelDTO modelDTO = fillModelDTO(1.0D);
        modelDTO.setStart(3163.8);
        modelDTO.setEnd(3728.6);
        SaveModelResponse saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        List<ModelDTO> modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 1, "size!=1");

        modelDTO = fillModelDTO(1.1D);
        modelDTO.setStart(3263.8);
        modelDTO.setEnd(3628.6);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 3, "size!=3");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3263.8, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3263.8 && modelDTOList.get(1).getEnd() == 3628.6, "2");
        assertTrue(modelDTOList.get(2).getStart() == 3628.6 && modelDTOList.get(2).getEnd() == 3728.6, "3");

        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.0), "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(1), 1.1), "2");
        assertTrue(checkModelDTOFields(modelDTOList.get(2), 1.0), "3");

        modelDTO = fillModelDTO(1.2D);
        modelDTO.setStart(3363.8);
        modelDTO.setEnd(3528.6);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 5, "size!=5");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3263.8, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3263.8 && modelDTOList.get(1).getEnd() == 3363.8, "2");
        assertTrue(modelDTOList.get(2).getStart() == 3363.8 && modelDTOList.get(2).getEnd() == 3528.6, "3");
        assertTrue(modelDTOList.get(3).getStart() == 3528.6 && modelDTOList.get(3).getEnd() == 3628.6, "4");
        assertTrue(modelDTOList.get(4).getStart() == 3628.6 && modelDTOList.get(4).getEnd() == 3728.6, "5");

        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.0), "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(1), 1.1), "2");
        assertTrue(checkModelDTOFields(modelDTOList.get(2), 1.2), "3");
        assertTrue(checkModelDTOFields(modelDTOList.get(3), 1.1), "4");
        assertTrue(checkModelDTOFields(modelDTOList.get(4), 1.0), "5");

        modelDTO = fillModelDTO(1.3D);
        modelDTO.setStart(3163.8);
        modelDTO.setEnd(3728.6);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 1, "size!=1");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3728.6, "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.3), "1");

    }

    @Test
    void testDel() {
        Long userId = userRepository.findByEmail("test@mail.ru").
                orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")).getId();
        Long projectId = projectRepository.findAllByUserId(userId).get(0).getId();
        ModelDTO modelDTO = fillModelDTO(1.0D);
        modelDTO.setStart(3163.8);
        modelDTO.setEnd(3728.6);
        SaveModelResponse saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        List<ModelDTO> modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 1, "size!=1");

        modelDTO = fillModelDTO(1.1D);
        modelDTO.setStart(3263.8);
        modelDTO.setEnd(3628.6);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 3, "size!=3");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3263.8, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3263.8 && modelDTOList.get(1).getEnd() == 3628.6, "2");
        assertTrue(modelDTOList.get(2).getStart() == 3628.6 && modelDTOList.get(2).getEnd() == 3728.6, "3");

        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.0), "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(1), 1.1), "2");
        assertTrue(checkModelDTOFields(modelDTOList.get(2), 1.0), "3");


        modelDTO = fillModelDTO(1.2D);
        modelDTO.setStart(3363.8);
        modelDTO.setEnd(3528.6);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 5, "size!=5");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3263.8, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3263.8 && modelDTOList.get(1).getEnd() == 3363.8, "2");
        assertTrue(modelDTOList.get(2).getStart() == 3363.8 && modelDTOList.get(2).getEnd() == 3528.6, "3");
        assertTrue(modelDTOList.get(3).getStart() == 3528.6 && modelDTOList.get(3).getEnd() == 3628.6, "4");
        assertTrue(modelDTOList.get(4).getStart() == 3628.6 && modelDTOList.get(4).getEnd() == 3728.6, "5");

        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.0), "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(1), 1.1), "2");
        assertTrue(checkModelDTOFields(modelDTOList.get(2), 1.2), "3");
        assertTrue(checkModelDTOFields(modelDTOList.get(3), 1.1), "4");
        assertTrue(checkModelDTOFields(modelDTOList.get(4), 1.0), "5");

        modelDTO = fillModelDTO(1.3D);
        modelDTO.setStart(3263.8);
        modelDTO.setEnd(3728.6);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 2, "size!=2");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3263.8, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3263.8 && modelDTOList.get(1).getEnd() == 3728.6, "2");

        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.0), "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(1), 1.3), "2");
    }

    @Test
    void testH() {
        Long userId = userRepository.findByEmail("test@mail.ru").
                orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")).getId();
        Long projectId = projectRepository.findAllByUserId(userId).get(0).getId();
        ModelDTO modelDTO = fillModelDTO(1.0D);
        modelDTO.setStart(3163.8);
        modelDTO.setEnd(3728.6);
        SaveModelResponse saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        List<ModelDTO> modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 1, "size!=1");

        modelDTO = fillModelDTO(1.1D);
        modelDTO.setStart(3263.8);
        modelDTO.setEnd(3628.6);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 3, "size!=3");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3263.8, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3263.8 && modelDTOList.get(1).getEnd() == 3628.6, "2");
        assertTrue(modelDTOList.get(2).getStart() == 3628.6 && modelDTOList.get(2).getEnd() == 3728.6, "3");

        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.0), "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(1), 1.1), "2");
        assertTrue(checkModelDTOFields(modelDTOList.get(2), 1.0), "3");


        modelDTO = fillModelDTO(1.2D);
        modelDTO.setStart(3363.8);
        modelDTO.setEnd(3528.6);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 5, "size!=5");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3263.8, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3263.8 && modelDTOList.get(1).getEnd() == 3363.8, "2");
        assertTrue(modelDTOList.get(2).getStart() == 3363.8 && modelDTOList.get(2).getEnd() == 3528.6, "3");
        assertTrue(modelDTOList.get(3).getStart() == 3528.6 && modelDTOList.get(3).getEnd() == 3628.6, "4");
        assertTrue(modelDTOList.get(4).getStart() == 3628.6 && modelDTOList.get(4).getEnd() == 3728.6, "5");

        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.0), "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(1), 1.1), "2");
        assertTrue(checkModelDTOFields(modelDTOList.get(2), 1.2), "3");
        assertTrue(checkModelDTOFields(modelDTOList.get(3), 1.1), "4");
        assertTrue(checkModelDTOFields(modelDTOList.get(4), 1.0), "5");

        modelDTO = fillModelDTO(1.3D);
        modelDTO.setStart(3163.8);
        modelDTO.setEnd(3638.6);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 2, "size!=2");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3638.6, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3638.6 && modelDTOList.get(1).getEnd() == 3728.6, "2");

        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.3), "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(1), 1.0), "2");
    }
    @Test
    void testOb() {
        Long userId = userRepository.findByEmail("test@mail.ru").
                orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")).getId();
        Long projectId = projectRepository.findAllByUserId(userId).get(0).getId();
        ModelDTO modelDTO = fillModelDTO(1.0D);
        modelDTO.setStart(3163.8);
        modelDTO.setEnd(3728.6);
        SaveModelResponse saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        List<ModelDTO> modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 1, "size!=1");

        modelDTO = fillModelDTO(1.1D);
        modelDTO.setStart(3263.8);
        modelDTO.setEnd(3628.6);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 3, "size!=3");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3263.8, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3263.8 && modelDTOList.get(1).getEnd() == 3628.6, "2");
        assertTrue(modelDTOList.get(2).getStart() == 3628.6 && modelDTOList.get(2).getEnd() == 3728.6, "3");

        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.0), "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(1), 1.1), "2");
        assertTrue(checkModelDTOFields(modelDTOList.get(2), 1.0), "3");


        modelDTO = fillModelDTO(1.2D);
        modelDTO.setStart(3363.8);
        modelDTO.setEnd(3528.6);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 5, "size!=5");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3263.8, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3263.8 && modelDTOList.get(1).getEnd() == 3363.8, "2");
        assertTrue(modelDTOList.get(2).getStart() == 3363.8 && modelDTOList.get(2).getEnd() == 3528.6, "3");
        assertTrue(modelDTOList.get(3).getStart() == 3528.6 && modelDTOList.get(3).getEnd() == 3628.6, "4");
        assertTrue(modelDTOList.get(4).getStart() == 3628.6 && modelDTOList.get(4).getEnd() == 3728.6, "5");

        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.0), "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(1), 1.1), "2");
        assertTrue(checkModelDTOFields(modelDTOList.get(2), 1.2), "3");
        assertTrue(checkModelDTOFields(modelDTOList.get(3), 1.1), "4");
        assertTrue(checkModelDTOFields(modelDTOList.get(4), 1.0), "5");

        modelDTO = fillModelDTO(1.3D);
        modelDTO.setStart(3163.8);
        modelDTO.setEnd(3628.6);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 2, "size!=2");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3628.6, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3628.6 && modelDTOList.get(1).getEnd() == 3728.6, "2");

        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.3), "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(1), 1.0), "2");
    }

    @Test
    void testOd() {
        Long userId = userRepository.findByEmail("test@mail.ru").
                orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")).getId();
        Long projectId = projectRepository.findAllByUserId(userId).get(0).getId();
        ModelDTO modelDTO = fillModelDTO(1.0D);
        modelDTO.setStart(3163.8);
        modelDTO.setEnd(3728.6);
        SaveModelResponse saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        List<ModelDTO> modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 1, "size!=1");

        modelDTO = fillModelDTO(1.1D);
        modelDTO.setStart(3263.8);
        modelDTO.setEnd(3628.6);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 3, "size!=3");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3263.8, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3263.8 && modelDTOList.get(1).getEnd() == 3628.6, "2");
        assertTrue(modelDTOList.get(2).getStart() == 3628.6 && modelDTOList.get(2).getEnd() == 3728.6, "3");

        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.0), "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(1), 1.1), "2");
        assertTrue(checkModelDTOFields(modelDTOList.get(2), 1.0), "3");


        modelDTO = fillModelDTO(1.2D);
        modelDTO.setStart(3263.8);
        modelDTO.setEnd(3628.6);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 3, "size!=3");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3263.8, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3263.8 && modelDTOList.get(1).getEnd() == 3628.6, "2");
        assertTrue(modelDTOList.get(2).getStart() == 3628.6 && modelDTOList.get(2).getEnd() == 3728.6, "3");

        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.0), "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(1), 1.2), "2");
        assertTrue(checkModelDTOFields(modelDTOList.get(2), 1.0), "3");

        modelDTO = fillModelDTO(1.3D);
        modelDTO.setStart(3163.8);
        modelDTO.setEnd(3263.8);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 3, "size!=3");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3263.8, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3263.8 && modelDTOList.get(1).getEnd() == 3628.6, "2");
        assertTrue(modelDTOList.get(2).getStart() == 3628.6 && modelDTOList.get(2).getEnd() == 3728.6, "3");

        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.3), "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(1), 1.2), "2");
        assertTrue(checkModelDTOFields(modelDTOList.get(2), 1.0), "3");

        modelDTO = fillModelDTO(1.4D);
        modelDTO.setStart(3628.6);
        modelDTO.setEnd(3728.6);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 3, "size!=3");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3263.8, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3263.8 && modelDTOList.get(1).getEnd() == 3628.6, "2");
        assertTrue(modelDTOList.get(2).getStart() == 3628.6 && modelDTOList.get(2).getEnd() == 3728.6, "3");

        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.3), "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(1), 1.2), "2");
        assertTrue(checkModelDTOFields(modelDTOList.get(2), 1.4), "3");


        modelDTO = fillModelDTO(1.5D);
        modelDTO.setStart(3363.8);
        modelDTO.setEnd(3528.6);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 5, "size!=5");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3263.8, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3263.8 && modelDTOList.get(1).getEnd() == 3363.8, "2");
        assertTrue(modelDTOList.get(2).getStart() == 3363.8 && modelDTOList.get(2).getEnd() == 3528.6, "3");
        assertTrue(modelDTOList.get(3).getStart() == 3528.6 && modelDTOList.get(3).getEnd() == 3628.6, "4");
        assertTrue(modelDTOList.get(4).getStart() == 3628.6 && modelDTOList.get(4).getEnd() == 3728.6, "5");

        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.3), "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(1), 1.2), "2");
        assertTrue(checkModelDTOFields(modelDTOList.get(2), 1.5), "3");
        assertTrue(checkModelDTOFields(modelDTOList.get(3), 1.2), "4");
        assertTrue(checkModelDTOFields(modelDTOList.get(4), 1.4), "5");

        modelDTO = fillModelDTO(1.6D);
        modelDTO.setStart(3263.8);
        modelDTO.setEnd(3363.8);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 5, "size!=5");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3263.8, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3263.8 && modelDTOList.get(1).getEnd() == 3363.8, "2");
        assertTrue(modelDTOList.get(2).getStart() == 3363.8 && modelDTOList.get(2).getEnd() == 3528.6, "3");
        assertTrue(modelDTOList.get(3).getStart() == 3528.6 && modelDTOList.get(3).getEnd() == 3628.6, "4");
        assertTrue(modelDTOList.get(4).getStart() == 3628.6 && modelDTOList.get(4).getEnd() == 3728.6, "5");

        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.3), "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(1), 1.6), "2");
        assertTrue(checkModelDTOFields(modelDTOList.get(2), 1.5), "3");
        assertTrue(checkModelDTOFields(modelDTOList.get(3), 1.2), "4");
        assertTrue(checkModelDTOFields(modelDTOList.get(4), 1.4), "5");
    }

    @Test
    @Transactional
    void testGetModel() {
        Long userId = userRepository.findByEmail("test@mail.ru").
                orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")).getId();
        Long projectId = projectRepository.findAllByUserId(userId).get(0).getId();
        ModelDTO modelDTO = fillModelDTO(1.0D);
        modelDTO.setStart(3163.8);
        modelDTO.setEnd(3728.6);
        SaveModelResponse saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        List<ModelDTO> modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 1, "size!=1");

        modelDTO = fillModelDTO(1.1D);
        modelDTO.setStart(3263.8);
        modelDTO.setEnd(3628.6);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 3, "size!=3");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3263.8, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3263.8 && modelDTOList.get(1).getEnd() == 3628.6, "2");
        assertTrue(modelDTOList.get(2).getStart() == 3628.6 && modelDTOList.get(2).getEnd() == 3728.6, "3");

        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.0), "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(1), 1.1), "2");
        assertTrue(checkModelDTOFields(modelDTOList.get(2), 1.0), "3");


        modelDTO = fillModelDTO(1.2D);
        modelDTO.setStart(3363.8);
        modelDTO.setEnd(3528.6);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 5, "size!=5");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3263.8, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3263.8 && modelDTOList.get(1).getEnd() == 3363.8, "2");
        assertTrue(modelDTOList.get(2).getStart() == 3363.8 && modelDTOList.get(2).getEnd() == 3528.6, "3");
        assertTrue(modelDTOList.get(3).getStart() == 3528.6 && modelDTOList.get(3).getEnd() == 3628.6, "4");
        assertTrue(modelDTOList.get(4).getStart() == 3628.6 && modelDTOList.get(4).getEnd() == 3728.6, "5");

        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.0), "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(1), 1.1), "2");
        assertTrue(checkModelDTOFields(modelDTOList.get(2), 1.2), "3");
        assertTrue(checkModelDTOFields(modelDTOList.get(3), 1.1), "4");
        assertTrue(checkModelDTOFields(modelDTOList.get(4), 1.0), "5");


        modelDTOList = modelService.getModel("test@mail.ru", projectId);
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 5, "size!=5");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3263.8, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3263.8 && modelDTOList.get(1).getEnd() == 3363.8, "2");
        assertTrue(modelDTOList.get(2).getStart() == 3363.8 && modelDTOList.get(2).getEnd() == 3528.6, "3");
        assertTrue(modelDTOList.get(3).getStart() == 3528.6 && modelDTOList.get(3).getEnd() == 3628.6, "4");
        assertTrue(modelDTOList.get(4).getStart() == 3628.6 && modelDTOList.get(4).getEnd() == 3728.6, "5");

        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.0), "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(1), 1.1), "2");
        assertTrue(checkModelDTOFields(modelDTOList.get(2), 1.2), "3");
        assertTrue(checkModelDTOFields(modelDTOList.get(3), 1.1), "4");
        assertTrue(checkModelDTOFields(modelDTOList.get(4), 1.0), "5");
    }

    @Test
    @Transactional
    void testState() {
        Long userId = userRepository.findByEmail("test@mail.ru").
                orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")).getId();
        Long projectId = projectRepository.findAllByUserId(userId).get(0).getId();
        ModelDTO modelDTO = fillModelDTO(1.0D);
        modelDTO.setStart(3163.8);
        modelDTO.setEnd(3728.6);
        SaveModelResponse saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        List<ModelDTO> modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 1, "size!=1");

        modelDTO = fillModelDTO(1.1D);
        modelDTO.setStart(3263.8);
        modelDTO.setEnd(3628.6);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 3, "size!=3");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3263.8, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3263.8 && modelDTOList.get(1).getEnd() == 3628.6, "2");
        assertTrue(modelDTOList.get(2).getStart() == 3628.6 && modelDTOList.get(2).getEnd() == 3728.6, "3");

        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.0), "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(1), 1.1), "2");
        assertTrue(checkModelDTOFields(modelDTOList.get(2), 1.0), "3");


        modelDTO = fillModelDTO(1.2D);
        modelDTO.setStart(3363.8);
        modelDTO.setEnd(3528.6);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 5, "size!=5");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3263.8, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3263.8 && modelDTOList.get(1).getEnd() == 3363.8, "2");
        assertTrue(modelDTOList.get(2).getStart() == 3363.8 && modelDTOList.get(2).getEnd() == 3528.6, "3");
        assertTrue(modelDTOList.get(3).getStart() == 3528.6 && modelDTOList.get(3).getEnd() == 3628.6, "4");
        assertTrue(modelDTOList.get(4).getStart() == 3628.6 && modelDTOList.get(4).getEnd() == 3728.6, "5");

        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.0), "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(1), 1.1), "2");
        assertTrue(checkModelDTOFields(modelDTOList.get(2), 1.2), "3");
        assertTrue(checkModelDTOFields(modelDTOList.get(3), 1.1), "4");
        assertTrue(checkModelDTOFields(modelDTOList.get(4), 1.0), "5");


        modelDTOList = projectService.getProjectState( projectId).getModelDTOList();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 5, "size!=5");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3263.8, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3263.8 && modelDTOList.get(1).getEnd() == 3363.8, "2");
        assertTrue(modelDTOList.get(2).getStart() == 3363.8 && modelDTOList.get(2).getEnd() == 3528.6, "3");
        assertTrue(modelDTOList.get(3).getStart() == 3528.6 && modelDTOList.get(3).getEnd() == 3628.6, "4");
        assertTrue(modelDTOList.get(4).getStart() == 3628.6 && modelDTOList.get(4).getEnd() == 3728.6, "5");

        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.0), "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(1), 1.1), "2");
        assertTrue(checkModelDTOFields(modelDTOList.get(2), 1.2), "3");
        assertTrue(checkModelDTOFields(modelDTOList.get(3), 1.1), "4");
        assertTrue(checkModelDTOFields(modelDTOList.get(4), 1.0), "5");
    }

    @Test
    void test5() {
        Long userId = userRepository.findByEmail("test@mail.ru").
                orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")).getId();
        Long projectId = projectRepository.findAllByUserId(userId).get(0).getId();
        ModelDTO modelDTO = fillModelDTO(1.0D);
        modelDTO.setStart(3163.8);
        modelDTO.setEnd(3728.6);
        SaveModelResponse saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        List<ModelDTO> modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 1, "size!=1");

        modelDTO = fillModelDTO(1.1D);
        modelDTO.setStart(3263.8);
        modelDTO.setEnd(3628.6);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 3, "size!=3");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3263.8, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3263.8 && modelDTOList.get(1).getEnd() == 3628.6, "2");
        assertTrue(modelDTOList.get(2).getStart() == 3628.6 && modelDTOList.get(2).getEnd() == 3728.6, "3");

        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.0), "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(1), 1.1), "2");
        assertTrue(checkModelDTOFields(modelDTOList.get(2), 1.0), "3");


        modelDTO = fillModelDTO(1.2D);
        modelDTO.setStart(3173.8);
        modelDTO.setEnd(3253.8);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 5, "size!=5");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3173.8, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3173.8 && modelDTOList.get(1).getEnd() == 3253.8, "2");
        assertTrue(modelDTOList.get(2).getStart() == 3253.8 && modelDTOList.get(2).getEnd() == 3263.8, "3");
        assertTrue(modelDTOList.get(3).getStart() == 3263.8 && modelDTOList.get(3).getEnd() == 3628.6, "4");
        assertTrue(modelDTOList.get(4).getStart() == 3628.6 && modelDTOList.get(4).getEnd() == 3728.6, "5");

        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.0), "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(1), 1.2), "2");
        assertTrue(checkModelDTOFields(modelDTOList.get(2), 1.0), "3");
        assertTrue(checkModelDTOFields(modelDTOList.get(3), 1.1), "4");
        assertTrue(checkModelDTOFields(modelDTOList.get(4), 1.0), "5");

        modelDTO = fillModelDTO(1.3D);
        modelDTO.setStart(3163.8);
        modelDTO.setEnd(3618.6);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 3, "size!=3");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3618.6, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3618.6 && modelDTOList.get(1).getEnd() == 3628.6, "2");
        assertTrue(modelDTOList.get(2).getStart() == 3628.6 && modelDTOList.get(2).getEnd() == 3728.6, "3");


        assertTrue(checkModelDTOFields(modelDTOList.get(0), 1.3), "1");
        assertTrue(checkModelDTOFields(modelDTOList.get(1), 1.1), "2");
        assertTrue(checkModelDTOFields(modelDTOList.get(2), 1.0), "3");

    }
}