package ru.mpei.brics.behaviours.networkElement;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.context.ApplicationContext;
import ru.mpei.brics.agents.NetworkElementAgent;
import ru.mpei.brics.behaviours.networkElement.ActivePowerImbalanceFSM;
import ru.mpei.brics.extention.ApplicationContextHolder;
import ru.mpei.brics.extention.configirationClasses.NetworkElementConfiguration;
import ru.mpei.brics.extention.dto.DroolsFitnessDto;
import ru.mpei.brics.extention.helpers.DFHelper;

import java.util.List;


@Slf4j
public class AnalyzeFrequency extends TickerBehaviour {

    ApplicationContext context = ApplicationContextHolder.getContext();
    private NetworkElementConfiguration cfg = ((NetworkElementAgent) myAgent).getCfg();
    double startTime;

    public AnalyzeFrequency(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {
        if (cfg.getF() >= 50.1 || cfg.getF() <= 49.9) {
            this.stop();
        }
    }

    @Override
    public int onEnd() {
        myAgent.addBehaviour(new ActivePowerImbalanceFSM(myAgent, this.getPeriod()));
        return 1;
    }
}

