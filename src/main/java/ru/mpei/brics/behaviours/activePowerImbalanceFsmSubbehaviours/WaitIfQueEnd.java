package ru.mpei.brics.behaviours.activePowerImbalanceFsmSubbehaviours;

import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WaitIfQueEnd extends WakerBehaviour {
    public WaitIfQueEnd(Agent a, long timeout) {
        super(a, timeout);
    }

    @Override
    public void onStart() {
        log.warn("Agent {} go to wait because que end", myAgent.getLocalName());
    }
}
