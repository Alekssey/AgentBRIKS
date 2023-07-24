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
        registerFirstState(new AnalyzeFrequency(myAgent, period), "firstBeh");
        registerState(new SendFitnessValue(myAgent), "sendFitness");
        registerState(new ReceiveFitnessValues(myAgent), "receiveFitness");
        registerState(new RegulateFrequency(myAgent, period), "regulateFrequency");
        registerLastState(new MockLastBeh(myAgent), "refreshFSM");

        registerDefaultTransition("firstBeh", "sendFitness");
        registerDefaultTransition("sendFitness", "receiveFitness");
        registerTransition("receiveFitness", "regulateFrequency", 1);
        registerTransition("receiveFitness", "refreshFSM", 2);
        registerDefaultTransition("regulateFrequency", "refreshFSM");

    }
}
