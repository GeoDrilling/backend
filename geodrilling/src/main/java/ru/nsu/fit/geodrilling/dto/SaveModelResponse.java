package ru.nsu.fit.geodrilling.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SaveModelResponse {
  List<ModelDTO> modelDTO;
  List<CurveDto> curveDtoList;
}
