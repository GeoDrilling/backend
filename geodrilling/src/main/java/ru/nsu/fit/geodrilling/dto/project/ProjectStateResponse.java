package ru.nsu.fit.geodrilling.dto.project;

import lombok.Builder;
import lombok.Data;
import ru.nsu.fit.geodrilling.entity.TrackEntity;

import java.util.List;

@Data
@Builder
public class ProjectStateResponse {

    private List<TrackEntity> tracks;
    // model parameters
}
