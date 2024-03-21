package ru.mpei.brics.behaviours.activePowerImbalanceFsmSubbehaviours;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.brics.agent.NetworkElementAgent;
import ru.mpei.brics.agentDetector.UDPAgentDetector;
import ru.mpei.brics.model.AgentsCommunicationProtocols;
import ru.mpei.brics.model.TransferDutyStatus;
import ru.mpei.brics.utils.JacksonHelper;

@Slf4j
public class SendSuccessMsg extends OneShotBehaviour {

    private UDPAgentDetector aDetector;
    public SendSuccessMsg(NetworkElementAgent a) {
        super(a);
        this.aDetector = a.getADetector();
    }

    @Override
    public void action() {
        ACLMessage msg = new ACLMessage();
        msg.setPerformative(ACLMessage.REQUEST);
        msg.setProtocol(AgentsCommunicationProtocols.NOTIFICATIONS.getValue());
        msg.setContent(JacksonHelper.toJackson(TransferDutyStatus.SUCCESS));

        this.aDetector.getActiveAgents().forEach(msg::addReceiver);
        myAgent.send(msg);
        log.warn("Agent {} send SUCCESS message", myAgent.getLocalName());
    }
}
