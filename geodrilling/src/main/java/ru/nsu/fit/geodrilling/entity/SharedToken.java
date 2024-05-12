package ru.nsu.fit.geodrilling.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SharedToken {
    @Id
    private Long id;
    @OneToOne
    @MapsId
    private ProjectEntity project;
    @Column(unique = true)
    private String token;
}
