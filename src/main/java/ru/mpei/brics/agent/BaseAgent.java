package ru.mpei.brics.agent;

import jade.core.Agent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import ru.mpei.brics.agentDetector.UDPAgentDetector;
import ru.mpei.brics.model.NetworkElementConfiguration;
import ru.mpei.brics.utils.CommunicationWithContext;
import ru.mpei.brics.utils.CommunicatorWith104Service;
import ru.mpei.brics.utils.knowledgeBase.DroolsCommunicator;

import java.io.FileNotFoundException;
import java.net.ConnectException;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class BaseAgent extends Agent {
    @Getter
    protected UDPAgentDetector aDetector = null;
    @Getter
    protected NetworkElementConfiguration cfg = null;
    protected CommunicatorWith104Service communicatorWith104;

    public BaseAgent() {
        this.communicatorWith104 = CommunicationWithContext.getBean(CommunicatorWith104Service.class).get();
    }

    protected void startUdpMonitoring(NetworkElementConfiguration cfg) {
        this.aDetector = new UDPAgentDetector(
                this,
                cfg.getIFace(),
                cfg.getUdpMonitoringPeriod(),
                cfg.getUdpMonitoringPort()
        );
        this.aDetector.startDiscovering();
        this.aDetector.startSending();
        this.aDetector.deadAgentRemoving();
    }

    protected void requestInitialDataFromModel(NetworkElementConfiguration cfg) {
        try {
            Map<String, Double> measurement = this.communicatorWith104.getMeasurement(List.of(cfg.getPMeasurementName()));
            if (measurement.containsKey(cfg.getPMeasurementName())) {
                cfg.setCurrentP(measurement.get(cfg.getPMeasurementName()));
            }
        } catch (NumberFormatException e) {
            this.aDetector.terminate();
        } catch (ResourceAccessException e) {
            log.error("ResourceAccessException occurred while connecting to the model");
            this.aDetector.terminate();
        } catch (ConnectException e) {
            log.error("ConnectException occurred while connecting to the model");
            this.aDetector.terminate();
        }
    }

    protected void identifyConnectingOfAgent(NetworkElementConfiguration cfg) {
        try {
            this.communicatorWith104.sendCommand(cfg.getPCommandName(), cfg.getCurrentP());
            this.communicatorWith104.sendCommand(cfg.getIdentifyingCommand(), 1.0);
        } catch (ResourceAccessException e) {
            log.error("ResourceAccessException occurred while connecting to the model");
            this.aDetector.terminate();
        } catch (ConnectException e) {
            log.error("ConnectException occurred while connecting to the model");
            this.aDetector.terminate();
        } catch (HttpServerErrorException e) {
            log.error("HttpServerErrorException occurred while connecting to the model");
            this.aDetector.terminate();
        }
    }

    protected void initializeKnowledgeBase(String rulesFileName) {
        try {
            DroolsCommunicator communicator = new DroolsCommunicator(rulesFileName, this.cfg);
            this.cfg.setKnowledgeBaseCommunicator(communicator);
        } catch (FileNotFoundException e) {
            log.error("Error occurred while initializing knowledge base");
            this.aDetector.terminate();
            throw new RuntimeException(e);
        }
    }
}
