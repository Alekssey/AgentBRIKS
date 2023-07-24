package ru.mpei.brics.behaviours.networkElement.activePowerImbalanceFSMSubbehaviours;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ru.mpei.brics.extention.configirationClasses.NetworkElementConfiguration;

public class WaitForNotification extends Behaviour {

    private MessageTemplate mt = null;

    @Override
    public void onStart() {
        this.mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(mt);
        if(msg != null) {

        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
