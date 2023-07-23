package ru.mpei.brics.test.agents;

import jade.core.Agent;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.brics.test.behaviours.ReceiveBehaviour;
import ru.mpei.brics.test.services.DFHelper;


@Slf4j
public class ReceiverAgent extends Agent {
    DFHelper df = new DFHelper();

    @Override
    protected void setup() {
        log.info("{} was born", this.getLocalName());
        df.registration(this, "receiver");
        this.addBehaviour(new ReceiveBehaviour(this));
    }
}
