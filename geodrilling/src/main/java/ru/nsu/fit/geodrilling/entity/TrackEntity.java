package ru.nsu.fit.geodrilling.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.nsu.fit.geodrilling.entity.state.CurveStateEntity;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TrackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToMany(mappedBy="trackEntity", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<CurveStateEntity> curvesStates;

    @ManyToOne
    @JoinColumn(name="project_id")
    private ProjectStateEntity projectState;
}
