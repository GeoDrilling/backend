package ru.nsu.fit.geodrilling.entity.projectstate;

import lombok.*;
import ru.nsu.fit.geodrilling.entity.projectstate.property.BaseProperty;

import java.util.List;
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GroupProperties {
    private String name;
    private List<BaseProperty> properties;
}
