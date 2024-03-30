package ru.nsu.fit.geodrilling.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RangeParameters {
  private Double min_tvd_start;
  private Double max_tvd_start;
  private Double min_alpha;
  private Double max_alpha;
  private Double min_ro_up;
  private Double max_ro_up;
  private Double min_ro_down;
  private Double max_ro_down;
  private Double min_kanisotropy_up;
  private Double max_kanisotropy_up;
  private Double min_kanisotropy_down;
  private Double max_kanisotropy_down;
}
