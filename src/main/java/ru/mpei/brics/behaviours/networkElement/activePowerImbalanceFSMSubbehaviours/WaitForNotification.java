package ru.mpei.brics.behaviours.networkElement.activePowerImbalanceFSMSubbehaviours;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ru.mpei.brics.extention.configirationClasses.NetworkElementConfiguration;
import ru.mpei.brics.extention.dto.AgentToAgentDto;
import ru.mpei.brics.extention.dto.TradeStatus;
import ru.mpei.brics.extention.helpers.JacksonHelper;

public class WaitForNotification extends Behaviour {

    private boolean doneFlg = false;
    private int behaviourResult;

    public WaitForNotification(Agent a) {
        super(a);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
        if(msg != null) {
            AgentToAgentDto dto = JacksonHelper.fromJackson(msg.getContent(), AgentToAgentDto.class);
            switch (dto.getStatus()) {
                case FAIL:
                    this.behaviourResult = 1;
//                    sendConfirmAnswer(msg);
                    break;
                case SUCCESS:
                    this.behaviourResult = 2;
                    break;
            }
            this.doneFlg = true;
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
        return this.doneFlg;
    }

    private void sendConfirmAnswer(ACLMessage msg) {
        ACLMessage answer = new ACLMessage();
        answer.setPerformative(ACLMessage.CONFIRM);
        answer.addReceiver(msg.getSender());
        myAgent.send(answer);
    }
}
