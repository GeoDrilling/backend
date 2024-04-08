package ru.nsu.fit.geodrilling.entity.projectstate.property;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GradientProperty extends BaseProperty {
    List<GradientColor> gradient;
}
