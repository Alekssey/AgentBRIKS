package ru.mpei.brics.behaviours.activePowerImbalanceFsmSubbehaviours;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.brics.agent.NetworkElementAgent;
import ru.mpei.brics.agentDetector.UDPAgentDetector;
import ru.mpei.brics.model.AgentsCommunicationProtocols;
import ru.mpei.brics.model.NetworkElementConfiguration;

import java.util.Random;

@Slf4j
public class SendFitnessValue extends OneShotBehaviour {
    private final NetworkElementConfiguration cfg;
    private final UDPAgentDetector aDetector;

    public SendFitnessValue(NetworkElementAgent a) {
        super(a);
        this.cfg = a.getCfg();
        this.aDetector = a.getADetector();
    }

    @Override
    public void action() {
        Double fitnessVal = this.cfg.getKnowledgeBaseCommunicator().getFitnessValue() + Math.random() * 0.0001;
        this.cfg.getAgentsQueue().put(fitnessVal, myAgent.getAID());

        if (!this.aDetector.getActiveAgents().isEmpty()) {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setProtocol(AgentsCommunicationProtocols.FITNESS_TRADES.getValue());
            msg.setContent(Double.toString(fitnessVal));
            this.aDetector.getActiveAgents().forEach(msg::addReceiver);
            myAgent.send(msg);
            log.warn("Agent {} send fitness val: {};", myAgent.getLocalName(), msg.getContent());
        } else {
            log.warn("No candidates to receive fitness. My fitness value = {}", fitnessVal);
        }

    }

}
