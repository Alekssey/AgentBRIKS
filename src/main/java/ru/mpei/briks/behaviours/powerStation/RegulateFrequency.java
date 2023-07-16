package ru.mpei.briks.behaviours.powerStation;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.briks.agents.NetworkElementAgent;
import ru.mpei.briks.extention.configirationClasses.StationConfiguration;
import ru.mpei.briks.extention.regulator.PiRegulator;
import ru.mpei.briks.extention.regulator.Regulator;

@Slf4j
public class RegulateFrequency extends TickerBehaviour {

    private StationConfiguration cfg = ((NetworkElementAgent) myAgent).getCfg();
    private Regulator regulator = new PiRegulator(5, 1.0);

    public RegulateFrequency(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {
        cfg.setCurrentGeneratingP(cfg.getCurrentGeneratingP() + regulator.getSupplement(50, cfg.getF()));
        if(cfg.getF() >49.9 && cfg.getF() <= 50.1) {
            this.stop();
        }
    }

    @Override
    public int onEnd() {
        myAgent.addBehaviour(new AnalyzeMeasurements(myAgent, this.getPeriod()));
        return 1;
    }
}
