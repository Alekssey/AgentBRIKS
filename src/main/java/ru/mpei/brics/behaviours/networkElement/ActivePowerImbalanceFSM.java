package ru.mpei.brics.behaviours.networkElement;

import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import ru.mpei.brics.behaviours.networkElement.activePowerImbalanceFSMSubbehaviours.*;

public class ActivePowerImbalanceFSM extends FSMBehaviour {
    // analyze frequency
    // send self fitness val
    // receive others fitness values
    // analyze all values and go to regulate beh or to waiting beh
    // regulate frequency
    // wait for successful message or initiate regulation message
    // send msg about success
    // send msg about unable to fix imbalance


    public ActivePowerImbalanceFSM(Agent a, long period) {
        super(a);
//        registerFirstState(new AnalyzeFrequency(myAgent, period), "analyze");
        registerFirstState(new SendFitnessValue(myAgent), "sendFitness");
        registerState(new ReceiveFitnessValues(myAgent), "receiveFitness");
        registerState(new RegulateFrequency(myAgent, period), "regulateFrequency");
        registerState(new SendSuccessMsg(myAgent), "sendSuccess");
        registerState(new SendFailMsg(myAgent), "sendFail");
        registerState(new WaitForNotification(myAgent), "notificationWaiting");
        registerLastState(new LastBeh(myAgent), "refreshFSM");

//        registerDefaultTransition("analyze", "sendFitness");
        registerDefaultTransition("sendFitness", "receiveFitness");

        registerTransition("receiveFitness", "regulateFrequency", 1);
        registerTransition("regulateFrequency", "sendSuccess", 1);      // regulate -> send success (1)
        registerTransition("regulateFrequency", "sendFail", 2);         // regulate -> send fail (2)
        registerDefaultTransition("sendFail", "notificationWaiting");
        registerDefaultTransition("sendSuccess", "refreshFSM");

        registerTransition("receiveFitness", "notificationWaiting", 2);
        registerTransition("notificationWaiting", "regulateFrequency", 1);// wait -> regulate (1)
        registerTransition("notificationWaiting", "refreshFSM", 2);// wait -> last (2)



        registerDefaultTransition("regulateFrequency", "refreshFSM");

    }
}
