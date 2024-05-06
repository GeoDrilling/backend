package ru.nsu.fit.geodrilling.entity.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.fit.geodrilling.entity.projectstate.ModelCurveGroupProperties;
@Slf4j
public class ModelCurveGroupsConverter implements AttributeConverter<ModelCurveGroupProperties, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public String convertToDatabaseColumn(ModelCurveGroupProperties modelCurveGroupProperties) {
        try {
            return objectMapper.writeValueAsString(modelCurveGroupProperties);
        } catch (JsonProcessingException jpe) {
            log.error(jpe.getMessage());
            return null;
        }
    }

    @Override
    public ModelCurveGroupProperties convertToEntityAttribute(String s) {
        try {
            return objectMapper.readValue(s, ModelCurveGroupProperties.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
