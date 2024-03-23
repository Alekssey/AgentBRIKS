package ru.mpei.brics.behaviours.activePowerImbalanceFsmSubbehaviours;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.brics.model.AgentsCommunicationProtocols;
import ru.mpei.brics.model.TransferDutyStatus;
import ru.mpei.brics.utils.JacksonHelper;

@Slf4j
public class WaitForNotification extends Behaviour {

    private boolean doneFlg = false;
    private int behaviourResult;
    private MessageTemplate mt;

    public WaitForNotification(Agent a) {
        super(a);
        this.mt = MessageTemplate.and(
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchProtocol(AgentsCommunicationProtocols.NOTIFICATIONS.getValue())
        );
    }

    @Override
    public void onStart() {
        log.warn("Agent {} go to wait for notification", myAgent.getLocalName());
    }

    @Override
    public void action() {
        ACLMessage request = myAgent.receive(this.mt);
        if(request != null) {
            log.warn("Agent {} receive msg {} from {}",myAgent.getLocalName(), request.getContent(), request.getSender().getLocalName());
            TransferDutyStatus status = JacksonHelper.fromJackson(request.getContent(), TransferDutyStatus.class);
            switch (status) {
                case SUCCESS:
                    this.behaviourResult = 1;
                    break;
                case FAIL:
                    this.behaviourResult = 2;
                    handleRequestMsg(request);
                    break;
                case LOAD_SUCCESS:
                    this.behaviourResult = 2;
                    handleRequestMsg(request);
                    break;
                case BLOCK:
                    this.behaviourResult = 3;
                    break;
            }
            this.doneFlg = true;
        } else {
            block();
        }
    }

    @Override
    public int onEnd() {
        this.doneFlg = false;
        log.error("onEnd method return {}", this.behaviourResult);
        return this.behaviourResult;
    }

    @Override
    public boolean done() {
        return this.doneFlg;
    }

    private void handleRequestMsg(ACLMessage request) {
        log.error("1");
        ACLMessage response = request.createReply();
        log.error("2");
        response.setPerformative(ACLMessage.INFORM);
        log.error("3");
        response.setProtocol(AgentsCommunicationProtocols.NOTIFICATIONS.getValue());
        log.error("4");
        response.setContent(JacksonHelper.toJackson(TransferDutyStatus.CONFIRM));
        log.error("5");
        myAgent.send(response);
        log.error("Agent {} send response {}", myAgent.getLocalName(), response.getContent());
    }
}
