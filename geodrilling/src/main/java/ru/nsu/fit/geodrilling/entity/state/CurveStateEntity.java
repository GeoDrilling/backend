package ru.nsu.fit.geodrilling.entity.state;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.nsu.fit.geodrilling.entity.CurveEntity;
import ru.nsu.fit.geodrilling.entity.TrackEntity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CurveStateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String color;

    private String name;

    @ManyToOne
    @JoinColumn(name="track_id")
    private TrackEntity trackEntity;
}
