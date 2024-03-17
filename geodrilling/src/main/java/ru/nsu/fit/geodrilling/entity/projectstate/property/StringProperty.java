package ru.nsu.fit.geodrilling.entity.projectstate.property;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class StringProperty extends BaseProperty {
    private String value;
    public StringProperty(String name, String value) {
        super(name);
        this.value = value;
    }
}
