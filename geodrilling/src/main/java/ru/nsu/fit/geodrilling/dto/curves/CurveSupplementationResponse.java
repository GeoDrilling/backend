package ru.nsu.fit.geodrilling.dto.curves;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CurveSupplementationResponse {
    private List<String> curvesNames;
}
