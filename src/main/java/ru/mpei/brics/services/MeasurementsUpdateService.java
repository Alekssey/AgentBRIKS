package ru.mpei.brics.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import ru.mpei.brics.model.ApplicationConfiguration;
import ru.mpei.brics.model.NetworkElementConfiguration;
import ru.mpei.brics.utils.CommunicatorWith104Service;

import javax.annotation.PostConstruct;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class MeasurementsUpdateService {
    @Autowired
    private CommunicatorWith104Service communicatorWith104;
    @Autowired
    private ApplicationConfiguration configuration;
    private final List<NetworkElementConfiguration> subscribers = new ArrayList<>();
    private final double[] buffer = new double[] {50.0, 50.0, 50.0};

    @PostConstruct
    private void startUpdatingMeasurements() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            try {
                Map<String, Double> measurement = this.communicatorWith104.getMeasurement(List.of("frequency"));
                handleFrequencyValue(measurement.get("frequency"));
            } catch (ResourceAccessException e) {
                log.error("ConnectException occurred while request measurements");
            } catch (ConnectException e) {
                log.error("ConnectException occurred while connecting to the model");
            }
        }, 1000, configuration.getUpdateMeasurementPeriod(), TimeUnit.MILLISECONDS);

    }
    private void handleFrequencyValue(double newFrequency) {
        log.info("received frequency value: {}", newFrequency);

        for (int i = 0; i < 2; i++) {
            buffer[i] = buffer[i + 1];
        }
        buffer[2] = newFrequency;
        double median = (Arrays.stream(buffer).sorted().toArray())[1];
        this.subscribers.forEach(subscriber -> subscriber.setF(median));
    }

    public synchronized void subscribe(NetworkElementConfiguration subscriber) {
        this.subscribers.add(subscriber);
    }

    public synchronized void unsubscribe(NetworkElementConfiguration subscriber) {
        this.subscribers.remove(subscriber);
    }
}
