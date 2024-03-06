package ru.nsu.fit.geodrilling.dto.project;

import lombok.Data;

import java.util.List;

@Data
public class TrackDTO {

    private List<CurveDTO> curves;
}
