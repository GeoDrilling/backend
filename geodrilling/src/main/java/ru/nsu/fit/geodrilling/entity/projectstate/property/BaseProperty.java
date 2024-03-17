package ru.nsu.fit.geodrilling.entity.projectstate.property;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = EnumProperty.class, name = "0"),
        @JsonSubTypes.Type(value = StringProperty.class, name = "1"),
        @JsonSubTypes.Type(value = NumberProperty.class, name = "2"),
        @JsonSubTypes.Type(value = ColorProperty.class, name = "3"),
})
public class BaseProperty {
    protected String name;
}
