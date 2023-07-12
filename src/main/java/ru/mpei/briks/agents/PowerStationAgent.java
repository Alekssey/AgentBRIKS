package ru.mpei.briks.agents;

import jade.core.Agent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import ru.mpei.briks.behaviours.powerStation.GetAndAnalyzeMeasurements;

@Slf4j
public class PowerStationAgent extends Agent {

    @Value("${agents.discretization.period}")
    long period;

    @Override
    protected void setup() {
        log.info("{} was born", this.getLocalName());
        this.addBehaviour(new GetAndAnalyzeMeasurements(this, 2000));

    }
}
