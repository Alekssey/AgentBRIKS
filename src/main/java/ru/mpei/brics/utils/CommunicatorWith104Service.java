package ru.mpei.brics.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import ru.mpei.brics.model.ApplicationConfiguration;
import ru.mpei.brics.model.CommandTO;
import ru.mpei.brics.model.Response;
import ru.mpei.brics.model.TSDBResponse;

import javax.annotation.PostConstruct;
import java.net.ConnectException;
import java.util.*;

@Slf4j
public class CommunicatorWith104Service {
    @Autowired
    ApplicationConfiguration cfg;
    private String sendUrl;
    private String getUrl;

    @PostConstruct
    private void createUrl() {
        this.getUrl = "http://" + cfg.getIp() + ":" + cfg.getPort() + "/request/measurements/last";
        this.sendUrl = "http://" + cfg.getIp() + ":" + cfg.getPort() + "/iec104/send/command";
    }
    public synchronized Map<String, Double> getMeasurement(List<String> tags) throws ResourceAccessException, ConnectException {
        Map<String, Double> measurements = new HashMap<>();
        ResponseEntity response = sendPostRequest(this.getUrl, JacksonHelper.toJackson(tags));
        try {
            TSDBResponse responseBody = JacksonHelper.fromJackson(response.getBody().toString(), TSDBResponse.class);
            for (Response r : responseBody.getResponses()) {
                measurements.put(r.getParamName(), Double.parseDouble(r.getValues().get(0)));
            }
        } catch (NumberFormatException e) {
            log.error("Error during parse http request body");
            throw new NumberFormatException();
        }
        return measurements;
    }

    public  void sendCommand(String tag, double value) throws ResourceAccessException, ConnectException, HttpServerErrorException {
        CommandTO body = new CommandTO(tag, Double.toString(value));
        ResponseEntity response = sendPostRequest(this.sendUrl, JacksonHelper.toJackson(body));
    }

    private synchronized ResponseEntity sendPostRequest(String url, String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForEntity(url, request, String.class);
    }
}
