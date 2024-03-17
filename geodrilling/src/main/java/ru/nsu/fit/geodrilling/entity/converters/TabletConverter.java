package ru.nsu.fit.geodrilling.entity.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.fit.geodrilling.entity.projectstate.TabletProperties;
@Slf4j
public class TabletConverter implements AttributeConverter<TabletProperties, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(TabletProperties tabletProperties) {
        try {
            return objectMapper.writeValueAsString(tabletProperties);
        } catch (JsonProcessingException jpe) {
            log.error(jpe.getMessage());
            return null;
        }
    }

    @Override
    public TabletProperties convertToEntityAttribute(String s) {
        try {
            return objectMapper.readValue(s, TabletProperties.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
