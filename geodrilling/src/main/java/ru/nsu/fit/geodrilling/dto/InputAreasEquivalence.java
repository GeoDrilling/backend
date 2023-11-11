package ru.nsu.fit.geodrilling.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class InputAreasEquivalence {
  public int nprobes;
  public int[] num_probe;
  public int npoints;
  public double[] tvd;
  public double[] x;
  public double[] zeni;
  public int n_tvd_start;
  public double[] tvd_start;
  public int n_alpha;
  public double[] alpha;
  public int n_ro_up;
  public double[] ro_up;
  public int n_kanisotropy_up;
  public double[] kanisotropy_up;
  public int n_ro_down;
  public double[] ro_down;
  public int n_kanisotropy_down;
  public double[] kanisotropy_down;
  public double[] phases;
  public double[] ampls;
}
