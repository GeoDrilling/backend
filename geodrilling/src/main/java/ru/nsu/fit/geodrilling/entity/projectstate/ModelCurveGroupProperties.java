package ru.nsu.fit.geodrilling.entity.projectstate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.nsu.fit.geodrilling.entity.projectstate.property.GradientColor;

import java.util.List;

@Getter
@Setter
@ToString
public class ModelCurveGroupProperties {
    private List<GroupProperties> properties;
    private List<GradientColor> gradient;
}
