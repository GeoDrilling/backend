package ru.nsu.fit.geodrilling.model;

import lombok.Data;

@Data
public class AreasEquivalence {
  private final double[] targetFunction;
  private final int status;

  public AreasEquivalence(double[] targetFunction, int status) {
    this.targetFunction = targetFunction;
    this.status = status;
  }

}
