package ru.nsu.fit.geodrilling.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InputBuildModel {
  public int nprobes;
  public int[] num_probe;
  public int npoints;
  public double[] md;
  public double[] tvd;
  public double[] x;
  public double[] zeni;
  public double[] ro_by_phases;
  public double[] ro_by_ampl;
  public double tvd_start;
  public double min_tvd_start;
  public double max_tvd_start;
  public double alpha;
  public double min_alpha;
  public double max_alpha;
  public double ro_up;
  public double kanisotropy_up;
  public double ro_down;
  public double kanisotropy_down;
}
