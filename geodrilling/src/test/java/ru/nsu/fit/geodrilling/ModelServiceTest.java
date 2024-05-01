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

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

@SpringBootTest
public class ModelServiceTest {
    @Autowired
    ModelService modelService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;
    @Test
    void test() {
        Long userId = userRepository.findByEmail("test@mail.ru").
                orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")).getId();
        Long projectId = projectRepository.findAllByUserId(userId).get(0).getId();
        ModelDTO modelDTO = new ModelDTO();
        modelDTO.setStart(3163.8);
        modelDTO.setEnd(3728.6);
        modelDTO.setAlpha(0D);
        modelDTO.setKanisotropyDown(1D);
        modelDTO.setKanisotropyUp(1D);
        modelDTO.setRoDown(0D);
        modelDTO.setRoUp(0D);
        modelDTO.setTvdStart(2307D);
        SaveModelResponse saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        List<ModelDTO> modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 1, "size!=1");
    }

    @Test
    void test2() {
        Long userId = userRepository.findByEmail("test@mail.ru").
                orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")).getId();
        Long projectId = projectRepository.findAllByUserId(userId).get(0).getId();
        ModelDTO modelDTO = new ModelDTO();
        modelDTO.setStart(3163.8);
        modelDTO.setEnd(3728.6);
        modelDTO.setAlpha(0D);
        modelDTO.setKanisotropyDown(1D);
        modelDTO.setKanisotropyUp(1D);
        modelDTO.setRoDown(0D);
        modelDTO.setRoUp(0D);
        modelDTO.setTvdStart(2307D);
        SaveModelResponse saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        List<ModelDTO> modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 1, "size!=1");

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

    }

    @Test
    void testRaz() {
        Long userId = userRepository.findByEmail("test@mail.ru").
                orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")).getId();
        Long projectId = projectRepository.findAllByUserId(userId).get(0).getId();
        ModelDTO modelDTO = new ModelDTO();
        modelDTO.setStart(3163.8);
        modelDTO.setEnd(3728.6);
        modelDTO.setAlpha(0D);
        modelDTO.setKanisotropyDown(1D);
        modelDTO.setKanisotropyUp(1D);
        modelDTO.setRoDown(0D);
        modelDTO.setRoUp(0D);
        modelDTO.setTvdStart(2307D);
        SaveModelResponse saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        List<ModelDTO> modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 1, "size!=1");

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

    }

    @Test
    void testR() {
        Long userId = userRepository.findByEmail("test@mail.ru").
                orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")).getId();
        Long projectId = projectRepository.findAllByUserId(userId).get(0).getId();
        ModelDTO modelDTO = new ModelDTO();
        modelDTO.setStart(3163.8);
        modelDTO.setEnd(3728.6);
        modelDTO.setAlpha(0D);
        modelDTO.setKanisotropyDown(1D);
        modelDTO.setKanisotropyUp(1D);
        modelDTO.setRoDown(0D);
        modelDTO.setRoUp(0D);
        modelDTO.setTvdStart(2307D);
        SaveModelResponse saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        List<ModelDTO> modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 1, "size!=1");

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

    }


    @Test
    void testL() {
        Long userId = userRepository.findByEmail("test@mail.ru").
                orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")).getId();
        Long projectId = projectRepository.findAllByUserId(userId).get(0).getId();
        ModelDTO modelDTO = new ModelDTO();
        modelDTO.setStart(3163.8);
        modelDTO.setEnd(3728.6);
        modelDTO.setAlpha(0D);
        modelDTO.setKanisotropyDown(1D);
        modelDTO.setKanisotropyUp(1D);
        modelDTO.setRoDown(0D);
        modelDTO.setRoUp(0D);
        modelDTO.setTvdStart(2307D);
        SaveModelResponse saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        List<ModelDTO> modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 1, "size!=1");

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
    }

    @Test
    void testRaz2AndD() {
        Long userId = userRepository.findByEmail("test@mail.ru").
                orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")).getId();
        Long projectId = projectRepository.findAllByUserId(userId).get(0).getId();
        ModelDTO modelDTO = new ModelDTO();
        modelDTO.setStart(3163.8);
        modelDTO.setEnd(3728.6);
        modelDTO.setAlpha(0D);
        modelDTO.setKanisotropyDown(1D);
        modelDTO.setKanisotropyUp(1D);
        modelDTO.setRoDown(0D);
        modelDTO.setRoUp(0D);
        modelDTO.setTvdStart(2307D);
        SaveModelResponse saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        List<ModelDTO> modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 1, "size!=1");

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

        modelDTO.setStart(3163.8);
        modelDTO.setEnd(3728.6);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 1, "size!=1");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3728.6, "1");
    }

    @Test
    void testDel() {
        Long userId = userRepository.findByEmail("test@mail.ru").
                orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")).getId();
        Long projectId = projectRepository.findAllByUserId(userId).get(0).getId();
        ModelDTO modelDTO = new ModelDTO();
        modelDTO.setStart(3163.8);
        modelDTO.setEnd(3728.6);
        modelDTO.setAlpha(0D);
        modelDTO.setKanisotropyDown(1D);
        modelDTO.setKanisotropyUp(1D);
        modelDTO.setRoDown(0D);
        modelDTO.setRoUp(0D);
        modelDTO.setTvdStart(2307D);
        SaveModelResponse saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        List<ModelDTO> modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 1, "size!=1");

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

        modelDTO.setStart(3263.8);
        modelDTO.setEnd(3728.6);
        saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 2, "size!=2");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3263.8, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3263.8 && modelDTOList.get(1).getEnd() == 3728.6, "2");
    }

    @Test
    void testOd() {
        Long userId = userRepository.findByEmail("test@mail.ru").
                orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")).getId();
        Long projectId = projectRepository.findAllByUserId(userId).get(0).getId();
        ModelDTO modelDTO = new ModelDTO();
        modelDTO.setStart(3163.8);
        modelDTO.setEnd(3728.6);
        modelDTO.setAlpha(0D);
        modelDTO.setKanisotropyDown(1D);
        modelDTO.setKanisotropyUp(1D);
        modelDTO.setRoDown(0D);
        modelDTO.setRoUp(0D);
        modelDTO.setTvdStart(2307D);
        SaveModelResponse saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        List<ModelDTO> modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 1, "size!=1");

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
    }

    @Test
    @Transactional
    void testGetModel() {
        Long userId = userRepository.findByEmail("test@mail.ru").
                orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")).getId();
        Long projectId = projectRepository.findAllByUserId(userId).get(0).getId();
        ModelDTO modelDTO = new ModelDTO();
        modelDTO.setStart(3163.8);
        modelDTO.setEnd(3728.6);
        modelDTO.setAlpha(0D);
        modelDTO.setKanisotropyDown(1D);
        modelDTO.setKanisotropyUp(1D);
        modelDTO.setRoDown(0D);
        modelDTO.setRoUp(0D);
        modelDTO.setTvdStart(2307D);
        SaveModelResponse saveModelResponse = modelService.saveModel("test@mail.ru", modelDTO, projectId);
        assertNotNull("saveModelResponse null", saveModelResponse);
        List<ModelDTO> modelDTOList = saveModelResponse.getModelDTO();
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 1, "size!=1");

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


        modelDTOList = modelService.getModel("test@mail.ru", projectId);
        assertNotNull("modelDTOList null", modelDTOList);
        assertTrue(modelDTOList.size() == 5, "size!=5");
        assertTrue(modelDTOList.get(0).getStart() == 3163.8 && modelDTOList.get(0).getEnd() == 3263.8, "1");
        assertTrue(modelDTOList.get(1).getStart() == 3263.8 && modelDTOList.get(1).getEnd() == 3363.8, "2");
        assertTrue(modelDTOList.get(2).getStart() == 3363.8 && modelDTOList.get(2).getEnd() == 3528.6, "3");
        assertTrue(modelDTOList.get(3).getStart() == 3528.6 && modelDTOList.get(3).getEnd() == 3628.6, "4");
        assertTrue(modelDTOList.get(4).getStart() == 3628.6 && modelDTOList.get(4).getEnd() == 3728.6, "5");
    }
}