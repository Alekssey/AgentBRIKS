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
    private NetworkElementConfiguration cfg;
    private AID receiverAgent = null;
    private int behaviourResult;

    public SendFailMsg(NetworkElementAgent a, ACLMessage msg) {
        super(a, msg);
        this.cfg = a.getCfg();
    }

    @Override
    public void onStart() {
        if ((this.receiverAgent = findNexInQueue(myAgent.getAID())) == null) {
            this.behaviourResult = 2;
        }
        super.onStart();
    }

    @Override
    protected Vector prepareRequests(ACLMessage request) {
        if (this.receiverAgent == null) return super.prepareRequests(request);
        request.setPerformative(ACLMessage.REQUEST);
        request.setProtocol(AgentsCommunicationProtocols.NOTIFICATIONS.getValue());
        request.addReceiver(this.receiverAgent);
        request.setContent(JacksonHelper.toJackson(TransferDutyStatus.FAIL));
        request.setReplyByDate(new Date(System.currentTimeMillis() + 1000));
        log.warn("Agent {} send FAIL message to {}", myAgent.getLocalName(), this.receiverAgent.getLocalName());
        return super.prepareRequests(request);
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
        return this.behaviourResult;
    }

    private AID findNexInQueue(AID aid) {
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