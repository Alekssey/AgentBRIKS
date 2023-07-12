package ru.mpei.briks.behaviours.grid;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.briks.extention.GridConfiguration;

import java.util.List;

@Slf4j
public class SpamMeasurement extends TickerBehaviour {
    GridConfiguration cfg;
    public SpamMeasurement(Agent a, long period, GridConfiguration cfg) {
        super(a, period);
        this.cfg = cfg;
    }

    @Override
    protected void onTick() {

        double frequency = 50 + 0.01 * (cfg.getGeneratedP() - cfg.getNecessaryP());
        cfg.setF(frequency);
        log.info("{} spam measurement: f={}, nP={}, nQ={}, gP={}, gQ={}", myAgent.getLocalName(),
                cfg.getF(), cfg.getNecessaryP(), cfg.getNecessaryQ(), cfg.getGeneratedP(), cfg.getGeneratedQ());

    }
}
