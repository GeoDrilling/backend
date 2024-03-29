package ru.nsu.fit.geodrilling.entity;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.nsu.fit.geodrilling.entity.converters.TabletConverter;
import ru.nsu.fit.geodrilling.entity.converters.TrackConverter;
import ru.nsu.fit.geodrilling.entity.projectstate.TabletProperties;
import ru.nsu.fit.geodrilling.entity.projectstate.TrackProperty;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class ProjectState {
    @Id
    private Long id;
    @JdbcTypeCode(SqlTypes.JSON)
    @Convert(converter = TabletConverter.class)
    private TabletProperties tabletProperties;
    @JdbcTypeCode(SqlTypes.JSON)
    @Convert(converter = TrackConverter.class)
    private List<TrackProperty> trackProperties;
}
