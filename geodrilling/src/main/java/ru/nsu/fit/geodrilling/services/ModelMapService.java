package ru.nsu.fit.geodrilling.services;

import org.springframework.stereotype.Service;
import ru.nsu.fit.geodrilling.dto.ModelDTO;
import ru.nsu.fit.geodrilling.entity.ModelEntity;

import java.util.ArrayList;
import java.util.List;

@Service
public class ModelMapService {
    private ModelDTO mapModelDto(ModelEntity modelEntity) {
        return new ModelDTO(modelEntity.getId(), modelEntity.getName(), modelEntity.getStartX(),
                modelEntity.getEndX(), modelEntity.getKanisotropyDown(), modelEntity.getRoDown(),
                modelEntity.getKanisotropyUp(), modelEntity.getRoUp(), modelEntity.getAlpha(),
                modelEntity.getTvdStart());
    }

    public List<ModelDTO> mapModelDtoList(List<ModelEntity> modelEntityList) {
        List<ModelDTO> modelDTOList = new ArrayList<>();
        for (ModelEntity modelEntity : modelEntityList) {
            modelDTOList.add(mapModelDto(modelEntity));
        }
        return modelDTOList;
    }
}
