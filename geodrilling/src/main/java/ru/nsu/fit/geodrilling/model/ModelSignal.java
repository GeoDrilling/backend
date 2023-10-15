package ru.nsu.fit.geodrilling.model;

import lombok.Getter;

@Getter
public class ModelSignal {
  private final double roDown;
  private final double kanisotropyDown;
  private final double[] syntRoByPhases;
  private final double[] syntRoByAmpl;
  private final int status;

  public ModelSignal(double roDown, double kanisotropyDown, double[] syntRoByPhases,
      double[] syntRoByAmpl, int status) {
    this.roDown = roDown;
    this.kanisotropyDown = kanisotropyDown;
    this.syntRoByPhases = syntRoByPhases;
    this.syntRoByAmpl = syntRoByAmpl;
    this.status = status;
  }
}
