package ru.mpei.brics.agent;

import lombok.extern.slf4j.Slf4j;
import ru.mpei.brics.behaviours.AnalyzeMeasurements;
import ru.mpei.brics.model.NetworkElementConfiguration;
import ru.mpei.brics.utils.XmlSerialization;

@Slf4j
public class NetworkElementAgent extends BaseAgent {

    @Override
    protected void setup() {
        String configFileName = (String) this.getArguments()[0];
        String rulesFileName = (String) this.getArguments()[1];
        log.warn("{} was born", this.getLocalName());

        this.cfg = XmlSerialization.unMarshalAny(
                NetworkElementConfiguration.class,
                "src/main/resources/agentsConfigs/" + configFileName);

        startUdpMonitoring(this.cfg);
        initializeKnowledgeBase(rulesFileName);
        requestInitialDataFromModel(this.cfg);
        identifyConnectingOfAgent(this.cfg);

        this.addBehaviour(new AnalyzeMeasurements(this, this.cfg.getBehavioursPeriod()));
    }
}
