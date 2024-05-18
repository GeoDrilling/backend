package ru.nsu.fit.geodrilling.mapper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.nsu.fit.geodrilling.dto.CurveDto;
import ru.nsu.fit.geodrilling.entity.CurveEntity;

@Component
@RequiredArgsConstructor
public class CurveMapper {

    private final Gson gson;
    public CurveDto map(CurveEntity curveEntity) {
        return new CurveDto(curveEntity.getName(), gson.fromJson(curveEntity.getData(),  new TypeToken<>(){}));
    }
}
