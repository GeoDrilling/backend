package ru.nsu.fit.geodrilling.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import ru.nsu.fit.geodrilling.model.User;

@Entity
@Table(name = "_las")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LasEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private ProjectEntity project;
}
