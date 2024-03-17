package ru.nsu.fit.geodrilling.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.nsu.fit.geodrilling.entity.projectstate.TabletProperties;
import ru.nsu.fit.geodrilling.entity.projectstate.TrackProperty;

import java.util.List;
@NoArgsConstructor
@Getter
@Setter
public class SaveProjectStateDTO {
    private TabletProperties tabletProperties;
    private List<TrackProperty> trackProperties;
}
