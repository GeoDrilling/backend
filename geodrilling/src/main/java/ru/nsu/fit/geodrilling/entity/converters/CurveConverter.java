package ru.nsu.fit.geodrilling.entity.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.fit.geodrilling.entity.projectstate.CurveProperty;
@Slf4j
public class CurveConverter implements AttributeConverter<CurveProperty, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(CurveProperty curveProperties) {
        try {
            return objectMapper.writeValueAsString(curveProperties);
        } catch (JsonProcessingException jpe) {
            log.error(jpe.getMessage());
            return null;
        }
    }

    @Override
    public CurveProperty convertToEntityAttribute(String s) {
        try {
            return objectMapper.readValue(s, CurveProperty.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
