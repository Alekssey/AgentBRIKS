package ru.mpei.brics.test.behaviours;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReceiveBehaviour extends Behaviour {

    MessageTemplate mt = null;
    public ReceiveBehaviour(Agent a) {
        super(a);
    }

    @Override
    public void onStart() {
        this.mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            log.info(msg.getContent());

        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
