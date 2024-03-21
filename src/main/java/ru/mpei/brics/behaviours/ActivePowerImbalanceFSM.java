package ru.mpei.brics.behaviours;

import jade.core.behaviours.FSMBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.brics.agent.NetworkElementAgent;
import ru.mpei.brics.behaviours.activePowerImbalanceFsmSubbehaviours.*;
import ru.mpei.brics.behaviours.activePowerImbalanceFsmSubbehaviours.regulators.RegulateFrequency;

@Slf4j
public class ActivePowerImbalanceFSM extends FSMBehaviour {
    private final String
            SEND_FITNESS = "send_fitness",
            RECEIVE_FITNESS = "receive_fitness",
            REGULATE_FREQUENCY = "regulate_frequency",
            SEND_SUCCESS = "send_success",
            SEND_FAIL = "send_fail",
            SEND_BLOCK = "send_block",
            NOTIFICATION_WAITING = "notification_waiting",
            WAIT_IF_QUE_END = "wait_if_que_end",
            END = "end";

    public ActivePowerImbalanceFSM(NetworkElementAgent a, long period) {
        super(a);
        registerFirstState(new SendFitnessValue(a), SEND_FITNESS);
        registerState(new ReceiveFitnessValue(a), RECEIVE_FITNESS);
        registerState(new WaitForNotification(a), NOTIFICATION_WAITING);
        registerState(new RegulateFrequency(a, a.getCfg().getBehavioursPeriod()), REGULATE_FREQUENCY);
        registerState(new SendSuccessMsg(a), SEND_SUCCESS);
        registerState(new SendFailMsg(a, new ACLMessage(ACLMessage.REQUEST)), SEND_FAIL);
        registerState(new SendBlockMsg(a), SEND_BLOCK);
        registerState(new WaitIfQueEnd(a, 30_000), WAIT_IF_QUE_END);
        registerLastState(new EndRegulation(a), END);


        registerDefaultTransition(SEND_FITNESS, RECEIVE_FITNESS);

        registerTransition(RECEIVE_FITNESS, REGULATE_FREQUENCY, 1);
        registerTransition(RECEIVE_FITNESS, NOTIFICATION_WAITING, 2);

        registerTransition(REGULATE_FREQUENCY, SEND_SUCCESS, 1);
        registerTransition(REGULATE_FREQUENCY, SEND_FAIL, 2);

        registerDefaultTransition(SEND_SUCCESS, END);

        registerTransition(SEND_FAIL, NOTIFICATION_WAITING, 1);
        registerTransition(SEND_FAIL, SEND_BLOCK, 2);

        registerDefaultTransition(SEND_BLOCK, WAIT_IF_QUE_END);

        registerTransition(NOTIFICATION_WAITING, END, 1);
        registerTransition(NOTIFICATION_WAITING, REGULATE_FREQUENCY, 2);
        registerTransition(NOTIFICATION_WAITING, WAIT_IF_QUE_END, 3);

        registerDefaultTransition(WAIT_IF_QUE_END, END);
    }
}