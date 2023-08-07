package ru.mpei.brics.behaviours.networkElement.activePowerImbalanceFSMSubbehaviours;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.brics.agents.NetworkElementAgent;
import ru.mpei.brics.extention.configirationClasses.NetworkElementConfiguration;

import java.util.Collections;

@Slf4j
public class ReceiveFitnessValues extends Behaviour {
    private MessageTemplate mt = null;
    private NetworkElementConfiguration cfg = ((NetworkElementAgent) myAgent).getCfg();

    public ReceiveFitnessValues(Agent a) {
        super(a);
    }

    private int msgCounter = 0;
    @Override
    public void onStart() {
        this.mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(mt);
        if(msg != null && msg.getProtocol().equals("initiatePowerTrade")) {
            msgCounter ++;
//            log.info("{} receive fitness val: {} from {} ", myAgent.getLocalName(), msg.getContent(), msg.getSender().getLocalName());
            double fitnessVal = Double.parseDouble(msg.getContent());
            this.cfg.setPTradeIsOpen(true);
            this.cfg.getAgentsQueue().put(fitnessVal, msg.getSender());
            this.cfg.getFitnessValues().add(fitnessVal);
            this.cfg.getFitnessValues().sort(Collections.reverseOrder());
        } else {
            block();
        }
    }

    @Override
    public int onEnd() {
        if(this.cfg.getAgentsQueue().get(this.cfg.getFitnessValues().get(0)).equals(myAgent.getAID())) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public boolean done() {
        return msgCounter == ((NetworkElementAgent) myAgent).getADetector().getActiveAgents().size();
    }
}
