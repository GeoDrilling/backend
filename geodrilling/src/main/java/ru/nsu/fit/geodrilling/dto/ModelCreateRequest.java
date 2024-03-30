package ru.nsu.fit.geodrilling.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ModelCreateRequest {
  ModelDTO modelDTO;
  RangeParameters rangeParameters;
}
