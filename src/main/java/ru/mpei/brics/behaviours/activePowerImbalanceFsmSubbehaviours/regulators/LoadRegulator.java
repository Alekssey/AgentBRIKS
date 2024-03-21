package ru.mpei.brics.behaviours.activePowerImbalanceFsmSubbehaviours.regulators;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

/** в будущем в этом классе будет поведение управления нагрузкой*/
public class LoadRegulator extends TickerBehaviour {
    public LoadRegulator(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {

    }
}
