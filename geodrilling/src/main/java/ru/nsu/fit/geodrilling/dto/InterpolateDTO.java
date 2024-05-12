package ru.nsu.fit.geodrilling.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class InterpolateDTO {
    public List<Double> depth = new ArrayList<>();
    public List<List<Double>> curves = new ArrayList<>();
}
