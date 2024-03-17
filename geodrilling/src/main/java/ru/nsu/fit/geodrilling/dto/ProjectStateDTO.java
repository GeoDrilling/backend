package ru.nsu.fit.geodrilling.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.nsu.fit.geodrilling.entity.projectstate.TabletProperties;
import ru.nsu.fit.geodrilling.entity.projectstate.TrackProperty;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectStateDTO {
    private Long id;
    private TabletProperties tabletProperties;
    private List<TrackProperty> trackProperties;
    private List<String> curvesNames;
}
