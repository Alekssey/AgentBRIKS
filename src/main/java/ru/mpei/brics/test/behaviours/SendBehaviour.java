package ru.mpei.brics.test.behaviours;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.brics.test.services.DFHelper;

@Slf4j
public class SendBehaviour extends TickerBehaviour {


    public SendBehaviour(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent("Hello");
        msg.setProtocol("info about generation");
        msg.addReceiver(DFHelper.findAgents(myAgent,"receiver").get(0));
        myAgent.send(msg);
        log.info("I send message");
    }
}
