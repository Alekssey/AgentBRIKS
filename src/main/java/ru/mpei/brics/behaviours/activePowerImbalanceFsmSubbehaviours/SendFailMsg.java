package ru.mpei.brics.behaviours.activePowerImbalanceFsmSubbehaviours;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.brics.agent.NetworkElementAgent;
import ru.mpei.brics.model.AgentsCommunicationProtocols;
import ru.mpei.brics.model.NetworkElementConfiguration;
import ru.mpei.brics.model.TransferDutyStatus;
import ru.mpei.brics.utils.JacksonHelper;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

@Slf4j
public class SendFailMsg extends AchieveREInitiator {
    protected NetworkElementConfiguration cfg;
    protected AID receiverAgent = null;
    protected int behaviourResult;

    public SendFailMsg(NetworkElementAgent a, ACLMessage msg) {
        super(a, msg);
        this.cfg = a.getCfg();
    }

    @Override
    public void onStart() {
        this.receiverAgent = initializeReceiver();
        super.onStart();
    }

    protected AID initializeReceiver() {
        AID aid = this.receiverAgent = findNexInQueue(myAgent.getAID());
        if (aid == null) {
            this.behaviourResult = 2;
        }
        return aid;
    }

    @Override
    protected Vector prepareRequests(ACLMessage request) {
        if (this.receiverAgent == null) return super.prepareRequests(request);
        if (request == null) request = new ACLMessage(ACLMessage.REQUEST);
//        request.setPerformative(ACLMessage.REQUEST);
        request.setProtocol(AgentsCommunicationProtocols.NOTIFICATIONS.getValue());
        request.addReceiver(this.receiverAgent);
        ACLMessage setupRequest = setContentToMessage(request);
        request.setReplyByDate(new Date(System.currentTimeMillis() + 1000));
        log.warn("Agent {} send FAIL message to {}", myAgent.getLocalName(), this.receiverAgent.getLocalName());
        return super.prepareRequests(setupRequest);
    }

    protected ACLMessage setContentToMessage(ACLMessage request) {
        request.setContent(JacksonHelper.toJackson(TransferDutyStatus.FAIL));
        return request;
    }

    @Override
    protected void handleAllResultNotifications(Vector resultNotifications) {
        if (!resultNotifications.isEmpty()) {
            TransferDutyStatus status = JacksonHelper.fromJackson(
                    ((ACLMessage) resultNotifications.get(0)).getContent(),
                    TransferDutyStatus.class);
            if (status.equals(TransferDutyStatus.CONFIRM)) this.behaviourResult = 1;
        } else if ((this.receiverAgent = findNexInQueue(this.receiverAgent)) != null) {
            this.reset(new ACLMessage(ACLMessage.REQUEST));
        } else {
            this.behaviourResult = 2;
        }
    }

    @Override
    public int onEnd() {
        this.reset();
        return this.behaviourResult;
    }

    protected AID findNexInQueue(AID aid) {
        boolean findNext = false;
        List<Double> fitnessValues = this.cfg.getAgentsQueue().keySet().stream().sorted(new Comparator<Double>() {
            @Override
            public int compare(Double o1, Double o2) {
                if (o1 > o2) return -1;
                else if (o1 < o2) return 1;
                return 0;
            }
        }).collect(Collectors.toList());
        for (double fitnessValue : fitnessValues) {
            if (findNext) {
                return this.cfg.getAgentsQueue().get(fitnessValue);
            }
            if (this.cfg.getAgentsQueue().get(fitnessValue).equals(aid)) findNext = true;
        }
        return null;
    }

}
