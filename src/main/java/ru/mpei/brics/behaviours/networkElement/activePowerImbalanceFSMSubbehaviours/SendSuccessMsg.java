package ru.mpei.brics.behaviours.networkElement.activePowerImbalanceFSMSubbehaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import ru.mpei.brics.agents.NetworkElementAgent;
import ru.mpei.brics.extention.dto.AgentToAgentDto;
import ru.mpei.brics.extention.dto.TradeStatus;
//import ru.mpei.brics.extention.helpers.DFHelper;
import ru.mpei.brics.extention.helpers.JacksonHelper;

public class SendSuccessMsg extends OneShotBehaviour {

    public SendSuccessMsg(Agent a) {
        super(a);
    }

    @Override
    public void action() {
        ACLMessage msg = new ACLMessage();
        msg.setPerformative(ACLMessage.REQUEST);
        msg.setProtocol("successful regulating");
        AgentToAgentDto dto = new AgentToAgentDto(TradeStatus.SUCCESS);
        msg.setContent(JacksonHelper.toJackson(dto));

//        for(AID aid : ((NetworkElementAgent) myAgent).getADetector().getActiveAgents()) {
//            msg.addReceiver(aid);
//        }

//        for(AID aid: DFHelper.findAgents(myAgent, "networkUnit")) {
//            if(!aid.equals(myAgent.getAID())) {
//                msg.addReceiver(aid);
//            }
//        }

        ((NetworkElementAgent) myAgent).getADetector().getActiveAgents().forEach(msg::addReceiver);
        myAgent.send(msg);
    }
}
