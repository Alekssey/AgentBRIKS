package ru.mpei.brics.agent;

import jade.core.Agent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import ru.mpei.brics.agentDetector.UDPAgentDetector;
import ru.mpei.brics.model.ElementsTypes;
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
    @Getter
    protected ElementsTypes elementType;

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
            if (this.elementType.equals(ElementsTypes.LOAD) && measurement.containsKey(cfg.getPMeasurementName())) {
                this.cfg.setCurrentStep(determinesStep(measurement.get(this.cfg.getPMeasurementName())));
                this.cfg.setCurrentP(this.cfg.getMaxP() * this.cfg.getLoadSteps().get(this.cfg.getCurrentStep()) / 100.0);
                log.error("Set load step {}", determinesStep(measurement.get(this.cfg.getPMeasurementName())));
            } else if (this.elementType.equals(ElementsTypes.SOURCE) && measurement.containsKey(cfg.getPMeasurementName())) {
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
/*            if (this.elementType.equals(ElementsTypes.SOURCE)) {
                this.communicatorWith104.sendCommand(cfg.getPCommandName(), cfg.getCurrentP());
            } else if (this.elementType.equals(ElementsTypes.LOAD)) {
                this.communicatorWith104.sendCommand(
                        cfg.getPCommandName(),
                        this.cfg.getMaxP() * this.cfg.getLoadSteps().get(this.cfg.getCurrentStep()) / 100.0);
            }*/
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

    protected int determinesStep(double power) {
        if (power < 0) {
            log.error("Incorrect power");
            return 0;
        }

        for (int i = 0 ; i < this.cfg.getLoadSteps().size() - 1; i++) {
            double currentStepPower = this.cfg.getMaxP() * this.cfg.getLoadSteps().get(i) / 100.0;
            double nextStepPower = this.cfg.getMaxP() * this.cfg.getLoadSteps().get(i + 1) / 100.0;
            if (power >= currentStepPower && power <= nextStepPower) {
                return (power - currentStepPower) < (nextStepPower - power) ? i : (i + 1);
            }
        }

        return this.cfg.getLoadSteps().size() - 1;
    }
}
