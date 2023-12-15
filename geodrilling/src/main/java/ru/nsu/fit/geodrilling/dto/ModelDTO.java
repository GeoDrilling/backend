package ru.nsu.fit.geodrilling.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.nsu.fit.geodrilling.model.OutputModel;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ModelDTO {
    private Long idModel;
    private String name;
    private OutputModel outputModel;
}
