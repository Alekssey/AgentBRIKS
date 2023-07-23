package ru.mpei.brics.behaviours.powerStation;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.brics.agents.NetworkElementAgent;
import ru.mpei.brics.extention.configirationClasses.NetworkElementConfiguration;

@Slf4j
public class ReceiveTradeRequest extends Behaviour {
    private MessageTemplate mt = null;
    private NetworkElementConfiguration cfg = ((NetworkElementAgent) myAgent).getCfg();

    public ReceiveTradeRequest(Agent a) {
        super(a);
    }

    @Override
    public void onStart() {
        this.mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(mt);
        if(msg != null && msg.getProtocol().equals("initiatePowerTrade")) {
            log.info("{} receive fitness val: {} from {} ", myAgent.getLocalName(), msg.getContent(), msg.getSender().getLocalName());
            this.cfg.setPTradeIsOpen(true);
        } else {
            block();
        }

    }

    @Override
    public boolean done() {
        return false;
    }
}
