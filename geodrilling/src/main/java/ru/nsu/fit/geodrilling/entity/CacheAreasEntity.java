package ru.nsu.fit.geodrilling.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.ByteArrayResource;

@Entity
@Table(name = "_cache")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CacheAreasEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double[] targetFunction;
    private String param1;
    private String param2;
    private int gridFrequency;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "model_id")
    private ModelEntity modelEntity;




}