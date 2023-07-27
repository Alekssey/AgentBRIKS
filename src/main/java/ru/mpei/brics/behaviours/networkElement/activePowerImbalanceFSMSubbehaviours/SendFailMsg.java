package ru.mpei.brics.behaviours.networkElement.activePowerImbalanceFSMSubbehaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import ru.mpei.brics.agents.NetworkElementAgent;
import ru.mpei.brics.extention.configirationClasses.NetworkElementConfiguration;
import ru.mpei.brics.extention.dto.AgentToAgentDto;
import ru.mpei.brics.extention.dto.TradeStatus;
import ru.mpei.brics.extention.helpers.JacksonHelper;

public class SendFailMsg extends OneShotBehaviour {

    private NetworkElementConfiguration cfg = ((NetworkElementAgent) myAgent).getCfg();
    public SendFailMsg(Agent a) {
        super(a);
    }

    @Override
    public void action() {
        ACLMessage msg = new ACLMessage();
        msg.setPerformative(ACLMessage.REQUEST);
        msg.setProtocol("successful regulating");
        AgentToAgentDto dto = new AgentToAgentDto(TradeStatus.FAIL);
        msg.setContent(JacksonHelper.toJackson(dto));
        msg.addReceiver(findNexInQueue());
        myAgent.send(msg);
    }

    private AID findNexInQueue() {
        AID nextAgent = null;

        for (int i = 0; i < this.cfg.getFitnessValues().size(); i++) {
            double fitness = this.cfg.getFitnessValues().get(i);
            if(this.cfg.getAgentsQueue().get(fitness).equals(myAgent.getAID())) {
                nextAgent = this.cfg.getAgentsQueue().get(this.cfg.getFitnessValues().get(i + 1));
                break;
            }
        }
        return nextAgent;
    }
}
