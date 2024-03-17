package ru.nsu.fit.geodrilling.entity.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.fit.geodrilling.entity.projectstate.TrackProperty;

@Slf4j
public class TrackConverter implements AttributeConverter<TrackProperty, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(TrackProperty trackProperties) {
        try {
            String result = objectMapper.writeValueAsString(trackProperties);
            System.out.println(result);
            return result;
        } catch (JsonProcessingException jpe) {
            log.error(jpe.getMessage());
            return null;
        }
    }

    @Override
    public TrackProperty convertToEntityAttribute(String s) {
        try {
            System.out.println(s);
            return objectMapper.readValue(s, TrackProperty.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}

