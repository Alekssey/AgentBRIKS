package ru.mpei.brics.behaviours.networkElement.activePowerImbalanceFSMSubbehaviours;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ReceiveConfirmMsg extends Behaviour {
    private int behaviourResult = 0;
    private boolean endBehaviour = false;

    public ReceiveConfirmMsg(Agent a) {
        super(a);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.CONFIRM));
        if(msg != null) {
            this.behaviourResult = 1;
            endBehaviour = true;
        } else {
            block();
        }
    }

    @Override
    public int onEnd() {
        return this.behaviourResult;
    }

    @Override
    public boolean done() {
        return this.endBehaviour;
    }
}
