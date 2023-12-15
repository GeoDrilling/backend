package ru.nsu.fit.geodrilling.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class OutputModel {
  private final double minKanisotropyDown;
  private final double maxKanisotropyDown;
  private final double[] syntRoByPhases;
  private final double[] syntRoByAmpl;
  private final double misfit;
  private final double kanisotropyDown;
  private final double minRoDown;
  private final double maxRoDown;
  private final double roDown;
  private final double kanisotropyUp;
  private final double minKanisotropyUp;
  private final double maxKanisotropyUp;
  private final double roUp;
  private final double minRoUp;
  private final double maxRoUp;
  private final double alpha;
  private final double tvdStart;
  private final int status;

  public OutputModel(double minKanisotropyDown, double maxKanisotropyDown,
      double[] syntRoByPhases, double[] syntRoByAmpl, double misfit, double kanisotropyDown,
      double minRoDown, double maxRoDown, double roDown, double kanisotropyUp,
      double minKanisotropyUp, double maxKanisotropyUp, double roUp, double minRoUp,
      double maxRoUp, double alpha, double tvdStart, int status) {
    this.minKanisotropyDown = minKanisotropyDown;
    this.maxKanisotropyDown = maxKanisotropyDown;
    this.syntRoByPhases = syntRoByPhases;
    this.syntRoByAmpl = syntRoByAmpl;
    this.misfit = misfit;
    this.kanisotropyDown = kanisotropyDown;
    this.minRoDown = minRoDown;
    this.maxRoDown = maxRoDown;
    this.roDown = roDown;
    this.kanisotropyUp = kanisotropyUp;
    this.minKanisotropyUp = minKanisotropyUp;
    this.maxKanisotropyUp = maxKanisotropyUp;
    this.roUp = roUp;
    this.minRoUp = minRoUp;
    this.maxRoUp = maxRoUp;
    this.alpha = alpha;
    this.tvdStart = tvdStart;
    this.status = status;
  }
}