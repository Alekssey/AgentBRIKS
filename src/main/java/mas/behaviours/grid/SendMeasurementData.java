package mas.behaviours.grid;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SendMeasurementData extends TickerBehaviour {


    public SendMeasurementData(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {
        log.trace("Grid spam measurement data");

    }
}
