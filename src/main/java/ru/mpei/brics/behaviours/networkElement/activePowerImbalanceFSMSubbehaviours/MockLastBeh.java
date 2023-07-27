package ru.mpei.brics.behaviours.networkElement.activePowerImbalanceFSMSubbehaviours;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.brics.agents.NetworkElementAgent;
import ru.mpei.brics.extention.configirationClasses.NetworkElementConfiguration;

@Slf4j
public class MockLastBeh extends OneShotBehaviour {

    private NetworkElementConfiguration cfg = ((NetworkElementAgent) myAgent).getCfg();


    public MockLastBeh(Agent a) {
        super(a);
    }

    @Override
    public void action() {
        log.info("\nFSM end. " +
                "\nFitness list: {}; " +
                "\nDeque map: {} " +
                "\nCurrent power: {}\n\n",
                cfg.getFitnessValues(), cfg.getAgentsQueue(), cfg.getCurrentP());

    }
}
