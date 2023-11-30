package ru.nsu.fit.geodrilling.dto.curves;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurveDataDownloadResponse {

    private String curveDataInJson;
}
