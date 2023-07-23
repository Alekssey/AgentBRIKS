package ru.mpei.brics.test.agents;

import jade.core.Agent;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.brics.test.behaviours.SendBehaviour;
import ru.mpei.brics.test.services.DFHelper;

@Slf4j
public class SenderAgent extends Agent {

    private DFHelper df =new DFHelper();

    @Override
    protected void setup() {
        log.info("{} was born", this.getLocalName());
        df.registration(this, "sender");
        this.addBehaviour(new SendBehaviour(this, 2000));
    }
}
