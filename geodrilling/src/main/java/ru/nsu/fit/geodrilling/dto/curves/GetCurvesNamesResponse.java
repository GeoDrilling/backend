package ru.nsu.fit.geodrilling.dto.curves;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetCurvesNamesResponse {
    private List<String> curvesNames;
}
