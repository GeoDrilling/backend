package ru.nsu.fit.geodrilling.entity.projectstate.property;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ColorProperty extends BaseProperty{
    private String value;
    public ColorProperty(String name, String value) {
        super(name);
        this.value = value;
    }
}
