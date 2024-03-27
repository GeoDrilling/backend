package ru.nsu.fit.geodrilling.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CurveDto {
  String name;
  double[] data;
}
