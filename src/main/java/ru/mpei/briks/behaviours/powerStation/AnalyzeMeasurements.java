package ru.mpei.briks.behaviours.powerStation;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.briks.agents.NetworkElementAgent;
import ru.mpei.briks.extention.configirationClasses.StationConfiguration;


@Slf4j
public class AnalyzeMeasurements extends TickerBehaviour {

    private StationConfiguration cfg = ((NetworkElementAgent) myAgent).getCfg();

    public AnalyzeMeasurements(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {
        if (cfg.getF() >= 50.1 || cfg.getF() <= 49.9) {
            this.stop();
        }
    }

    @Override
    public int onEnd() {
        myAgent.addBehaviour(new RegulateFrequency(myAgent, this.getPeriod()));
        return 1;
    }
}
