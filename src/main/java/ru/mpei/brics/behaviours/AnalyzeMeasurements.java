package ru.mpei.brics.behaviours;

import jade.core.behaviours.TickerBehaviour;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.brics.agent.NetworkElementAgent;
import ru.mpei.brics.model.NetworkElementConfiguration;


@Slf4j
public class AnalyzeMeasurements extends TickerBehaviour {

    private final NetworkElementConfiguration cfg;
    NetworkElementAgent myAgent;
    double startTime;


    public AnalyzeMeasurements(NetworkElementAgent a, long period) {
        super(a, period);
        this.myAgent = a;
        this.cfg = a.getCfg();
    }

    @Override
    protected void onTick() {
        if (cfg.getF() >= cfg.getTargetFreq() + cfg.getDeltaFreq()
                || cfg.getF() <= cfg.getTargetFreq() - cfg.getDeltaFreq()) {
            this.stop();
        }
        log.error("freq is ok");
    }

    @Override
    public int onEnd() {
        this.cfg.setRegulationStartTime(System.currentTimeMillis());
        log.warn("necessary to start regulation process");
        myAgent.addBehaviour(new ActivePowerImbalanceFSM(myAgent, this.cfg.getBehavioursPeriod()));
        return 1;
    }
}

