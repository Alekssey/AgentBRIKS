package mas.agents;

import jade.core.Agent;
import lombok.extern.slf4j.Slf4j;
import mas.behaviours.grid.SendMeasurementData;


@Slf4j
public class GridAgent extends Agent {

    @Override
    protected void setup() {
        log.trace("Agent {} was born", this.getLocalName());
        this.addBehaviour(new SendMeasurementData(this, 1000));
    }
}
