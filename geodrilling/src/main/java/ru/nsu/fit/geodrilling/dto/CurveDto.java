package ru.nsu.fit.geodrilling.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class CurveDto {
  String name;
  List<Double> data;
}
