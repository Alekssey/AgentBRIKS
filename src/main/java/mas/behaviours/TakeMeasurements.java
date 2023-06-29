package mas.behaviours;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TakeMeasurements extends TickerBehaviour {
    public TakeMeasurements(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {

        log.trace("behaviour created");
    }
}
