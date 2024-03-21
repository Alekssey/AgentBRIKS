package ru.mpei.brics.behaviours.activePowerImbalanceFsmSubbehaviours;

import jade.core.AID;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.WakerBehaviour;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.brics.agent.NetworkElementAgent;
import ru.mpei.brics.model.NetworkElementConfiguration;

@Slf4j
public class ReceiveFitnessValue extends ParallelBehaviour {

    private NetworkElementConfiguration cfg;
    private ReceivingFitnessSubBehaviour receivingSubBehaviour;

    public ReceiveFitnessValue(NetworkElementAgent a) {
        super(a, WHEN_ANY);
        this.cfg = a.getCfg();
        this.receivingSubBehaviour = new ReceivingFitnessSubBehaviour(a);
    }

    @Override
    public void onStart() {
        addSubBehaviour(this.receivingSubBehaviour);
        addSubBehaviour(new WakerBehaviour(myAgent, 3000) {
            @Override
            protected void onWake() {
                super.onWake();
            }
        });
    }

    @Override
    public int onEnd() {
        printTradesPartners();
        return receivingSubBehaviour.onEnd();
    }

    private void printTradesPartners() {
        StringBuilder sb = new StringBuilder("Partners: [");
        for (AID aid : this.cfg.getAgentsQueue().values()) {
            sb.append(aid.getLocalName()).append("; ");
        }
        sb.append("]");
        log.warn(sb.toString());
    }
}
