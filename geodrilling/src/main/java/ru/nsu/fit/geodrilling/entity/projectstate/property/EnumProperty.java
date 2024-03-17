package ru.nsu.fit.geodrilling.entity.projectstate.property;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.nsu.fit.geodrilling.entity.projectstate.enums.EnumType;
@Getter
@Setter
@ToString
@NoArgsConstructor
public class EnumProperty extends BaseProperty {
    private String value;
    private EnumType enumType;

    public EnumProperty(String name, String value, EnumType enumType) {
        super(name);
        this.value = value;
        this.enumType = enumType;
    }
}
