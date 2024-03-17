package ru.nsu.fit.geodrilling.entity.projectstate.property;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class NumberProperty extends BaseProperty {
    private Double value;

    public NumberProperty(String name, Double value) {
        super(name);
        this.value = value;
    }
}
