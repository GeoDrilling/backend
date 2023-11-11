package ru.nsu.fit.geodrilling.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class InputModelSignal {
  public int nprobes;
  public int[] num_probe;
  public int npoints;
  public double[] tvd;
  public double[] x;
  public double[] zeni;
  public double tvd_start;
  public double alpha;
  public double ro_up;
  public double kanisotropy_up;
  public double ro_down;
  public double kanisotropy_down;
}
