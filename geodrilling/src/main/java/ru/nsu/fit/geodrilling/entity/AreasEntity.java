package ru.nsu.fit.geodrilling.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "_areas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AreasEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double[] targetFunction;
    private int status;
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    private ModelEntity modelEntity;


}
