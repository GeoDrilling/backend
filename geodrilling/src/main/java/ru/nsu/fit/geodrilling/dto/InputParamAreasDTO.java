package ru.nsu.fit.geodrilling.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class InputParamAreasDTO {
    public String param1;
    public String param2;
    public Integer range;
    public Double colorMin;
    public Double colorMax;
    public double[] level;
}
