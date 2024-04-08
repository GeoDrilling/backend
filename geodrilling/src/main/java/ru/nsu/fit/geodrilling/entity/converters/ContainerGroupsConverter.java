package ru.nsu.fit.geodrilling.entity.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.fit.geodrilling.entity.projectstate.ContainerGroupProperties;
@Slf4j
public class ContainerGroupsConverter implements AttributeConverter<ContainerGroupProperties, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(ContainerGroupProperties containerGroupProperties) {
        try {
            return objectMapper.writeValueAsString(containerGroupProperties);
        } catch (JsonProcessingException jpe) {
            log.error(jpe.getMessage());
            return null;
        }
    }

    @Override
    public ContainerGroupProperties convertToEntityAttribute(String s) {
        try {
            return objectMapper.readValue(s, ContainerGroupProperties.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
