package ru.mpei.briks.behaviours.powerStation;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetAndAnalyzeMeasurements extends TickerBehaviour {

    public GetAndAnalyzeMeasurements(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {
        log.info("{} get and analyze measurements", myAgent.getLocalName());

    }
}
