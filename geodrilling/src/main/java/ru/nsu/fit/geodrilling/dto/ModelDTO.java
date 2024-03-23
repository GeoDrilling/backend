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
    private Double start;
    private Double end;
    private Double kanisotropyDown;
    private Double roDown;
    private Double kanisotropyUp;
    private Double roUp;
    private Double alpha;
    private Double tvdStart;
}
