package ru.mpei.brics.behaviours.networkElement.activePowerImbalanceFSMSubbehaviours;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.context.ApplicationContext;
import ru.mpei.brics.agents.NetworkElementAgent;
import ru.mpei.brics.extention.ApplicationContextHolder;
import ru.mpei.brics.extention.configirationClasses.NetworkElementConfiguration;
import ru.mpei.brics.extention.dto.DroolsFrequencyAllowDto;
import ru.mpei.brics.extention.regulator.PiRegulator;
import ru.mpei.brics.extention.regulator.Regulator;

@Slf4j
public class RegulateFrequency extends TickerBehaviour {

    private NetworkElementConfiguration cfg = ((NetworkElementAgent) myAgent).getCfg();
    private Regulator regulator = new PiRegulator(cfg.getKp(), cfg.getKi());
    private int behaviourResult;

    public RegulateFrequency(Agent a, long period) {
        super(a, period);
    }

    @Override
    public void onStart() {
        if(((NetworkElementAgent) myAgent).getKieSession() == null) {
            ApplicationContext context = ApplicationContextHolder.getContext();
            KieContainer kieContainer = (KieContainer) context.getBean("kieContainer");
            KieSession kieSession = kieContainer.newKieSession();
            ((NetworkElementAgent) myAgent).setKieSession(kieSession);
        }
    }

    @Override
    protected void onTick() {
        DroolsFrequencyAllowDto dto = new DroolsFrequencyAllowDto(
                myAgent.getLocalName(), cfg.getMaxP(), cfg.getCurrentP(), cfg.getF());
        ((NetworkElementAgent) myAgent).getKieSession().insert(dto);
        ((NetworkElementAgent) myAgent).getKieSession().fireAllRules();
//        log.error("Allow signal: {}", dto.isAllow());
        if(dto.isAllow()) {
            double supplement = regulator.getSupplement(cfg.getTargetFreq(), cfg.getF());
            if(cfg.getCurrentP() + supplement > cfg.getMaxP()) {
                cfg.setCurrentP(cfg.getMaxP());
                this.behaviourResult = 2;
                this.stop();
            } else if (cfg.getCurrentP() + supplement <= 0) {
                cfg.setCurrentP(0);
                this.behaviourResult = 2;
                this.stop();
            } else {
                cfg.setCurrentP(cfg.getCurrentP() + supplement);
            }
            if(cfg.getF() >= cfg.getTargetFreq() - cfg.getDeltaFreq()
                    && cfg.getF() <= cfg.getTargetFreq() + cfg.getDeltaFreq()) {
                this.behaviourResult = 1;
                this.stop();
            }
        } else {
            this.behaviourResult = 2;
            this.stop();
        }
    }

    @Override
    public int onEnd() {
        return behaviourResult;
    }
}
