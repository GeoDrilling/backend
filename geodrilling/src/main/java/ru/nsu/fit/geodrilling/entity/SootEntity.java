package ru.nsu.fit.geodrilling.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.nsu.fit.geodrilling.model.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "_soot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SootEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ROPL;
    private String ROPLD;
    private String ROPLE;
    private String ROPH;
    private String ROPHD;
    private String ROPHE;
    private String ROAL;
    private String ROALD;
    private String ROALE;
    private String ROAH;
    private String ROAHD;
    private String ROAHE;
    private String md;
    private String tvd;
    private String x;
    private String zeni;
    private Integer ROPLp;
    private Integer ROPLDp;
    private Integer ROPLEp;
    private Integer ROPHp;
    private Integer ROPHDp;
    private Integer ROPHEp;
    private Integer ROALp;
    private Integer ROALDp;
    private Integer ROALEp;
    private Integer ROAHp;
    private Integer ROAHDp;
    private Integer ROAHEp;
    private Integer mdp;
    private Integer tvdp;
    private Integer xp;
    private Integer zenip;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private ProjectEntity projectEntity;
}
