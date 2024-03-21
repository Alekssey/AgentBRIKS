package ru.mpei.brics.behaviours.activePowerImbalanceFsmSubbehaviours;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.brics.agent.NetworkElementAgent;
import ru.mpei.brics.agentDetector.UDPAgentDetector;
import ru.mpei.brics.model.AgentsCommunicationProtocols;
import ru.mpei.brics.model.NetworkElementConfiguration;

import java.util.Comparator;

@Slf4j
public class ReceivingFitnessSubBehaviour extends Behaviour {
    private MessageTemplate mt = null;
    private final NetworkElementConfiguration cfg;
    private final UDPAgentDetector agentDetector;
    private int msgCounter = 0;

    public ReceivingFitnessSubBehaviour(NetworkElementAgent a) {
        super(a);
        this.cfg = a.getCfg();
        this.agentDetector = a.getADetector();

    }

    @Override
    public void onStart() {
        this.mt = MessageTemplate.and(
                MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                MessageTemplate.MatchProtocol(AgentsCommunicationProtocols.FITNESS_TRADES.getValue())
        );
    }

    @Override
    public void action() {

        if(agentDetector.getActiveAgents().isEmpty()) return;

        ACLMessage msg = myAgent.receive(mt);
        if(msg != null) {
            msgCounter ++;
            double fitnessVal = Double.parseDouble(msg.getContent());
            this.cfg.getAgentsQueue().put(fitnessVal, msg.getSender());
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return msgCounter == this.agentDetector.getActiveAgents().size();
    }

    @Override
    public int onEnd() {
        double bestFitness = this.cfg.getAgentsQueue().keySet().stream().max(new Comparator<Double>() {
            @Override
            public int compare(Double o1, Double o2) {
                if (o1 > o2) return 1;
                else if (o1 < o2) return -1;
                return 0;
            }
        }).get();

        if (this.cfg.getAgentsQueue().get(bestFitness).equals(myAgent.getAID())) {
            return 1;
        } else {
            return 2;
        }
    }

}
