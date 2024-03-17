package ru.nsu.fit.geodrilling.entity.projectstate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
@Getter
@Setter
@ToString
public class CurveProperty {
    private String name;
    private List<GroupProperties> properties;
}
