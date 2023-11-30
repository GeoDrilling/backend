package ru.nsu.fit.geodrilling.dto.curves;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetCurvesNamesResponse {
    private List<String> curvesNames;
}
