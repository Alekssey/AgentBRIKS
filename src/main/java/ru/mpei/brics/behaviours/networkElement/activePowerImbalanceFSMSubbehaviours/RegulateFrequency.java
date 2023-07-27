package ru.mpei.brics.behaviours.networkElement.activePowerImbalanceFSMSubbehaviours;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.brics.agents.NetworkElementAgent;
import ru.mpei.brics.behaviours.networkElement.activePowerImbalanceFSMSubbehaviours.AnalyzeFrequency;
import ru.mpei.brics.extention.configirationClasses.NetworkElementConfiguration;
import ru.mpei.brics.extention.regulator.PiRegulator;
import ru.mpei.brics.extention.regulator.Regulator;

@Slf4j
public class RegulateFrequency extends TickerBehaviour {

    private NetworkElementConfiguration cfg = ((NetworkElementAgent) myAgent).getCfg();
    private Regulator regulator = new PiRegulator(10, 1.0);
    private int behaviourResult;

    public RegulateFrequency(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {
        double supplement = regulator.getSupplement(50, cfg.getF());
        if(cfg.getCurrentP() + supplement > cfg.getMaxP()) {
            cfg.setCurrentP(cfg.getMaxP());
            this.behaviourResult = 2;
            this.stop();
        } else {
            cfg.setCurrentP(cfg.getCurrentP() + supplement);
        }
        if(cfg.getF() >49.9 && cfg.getF() <= 50.1) {
            this.behaviourResult = 1;
            this.stop();
        }
    }

    @Override
    public int onEnd() {
        return behaviourResult;
    }
}
