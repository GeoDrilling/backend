package ru.nsu.fit.geodrilling.services.drawingAreasEquivalent;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
@Service
public class PythonService {
    public ByteArrayResource sendIntensityDataAndReceiveImage(
            List<Float> intensities, int n, double[] param1, double[] param2,
            String param1Name, String param2Name,
            Double colorMin, Double colorMax, double[] level) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> map = new HashMap<>();
        map.put("n", n);
        map.put("intensities", intensities);
        map.put("param1", param1);
        map.put("param2", param2);
        map.put("param1Name", param1Name);
        map.put("param2Name", param2Name);
        map.put("colorMin", colorMin);
        map.put("colorMax", colorMax);
        map.put("level", level);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(map, headers);
        String url = "http://localhost:5000/getColorMap";

        ResponseEntity<ByteArrayResource> response = restTemplate.postForEntity(url, request, ByteArrayResource.class);


        return response.getBody();
    }
}