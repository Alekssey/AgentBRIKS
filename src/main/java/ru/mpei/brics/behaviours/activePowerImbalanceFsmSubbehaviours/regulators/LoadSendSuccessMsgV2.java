package ru.mpei.brics.behaviours.activePowerImbalanceFsmSubbehaviours.regulators;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.brics.agent.NetworkElementAgent;
import ru.mpei.brics.behaviours.activePowerImbalanceFsmSubbehaviours.SendFailMsg;
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
public class LoadSendSuccessMsgV2 extends SendFailMsg {


    public LoadSendSuccessMsgV2(NetworkElementAgent a, ACLMessage msg) {
        super(a, msg);
    }

    @Override
    protected AID initializeReceiver() {
        double bestFitness = this.cfg.getAgentsQueue().keySet().stream().max(new Comparator<Double>() {
            @Override
            public int compare(Double o1, Double o2) {
                if (o1 > o2) return 1;
                else if (o1 < o2) return -1;
                return 0;
            }
        }).get();
        return this.cfg.getAgentsQueue().get(bestFitness);
    }

    @Override
    protected ACLMessage setContentToMessage(ACLMessage request) {
        request.setContent(JacksonHelper.toJackson(TransferDutyStatus.LOAD_SUCCESS));
        return request;
    }
}
