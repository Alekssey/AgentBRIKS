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
    private Regulator regulator = new PiRegulator(5, 1.0);

    public RegulateFrequency(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {
        cfg.setCurrentP(cfg.getCurrentP() + regulator.getSupplement(50, cfg.getF()));
        if(cfg.getF() >49.9 && cfg.getF() <= 50.1) {
            this.stop();
        }
    }

    @Override
    public int onEnd() {
        myAgent.addBehaviour(new AnalyzeFrequency(myAgent, this.getPeriod()));
        return 1;
    }
}
