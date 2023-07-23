package ru.mpei.brics.behaviours.powerStation;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.context.ApplicationContext;
import ru.mpei.brics.agents.NetworkElementAgent;
import ru.mpei.brics.extention.ApplicationContextHolder;
import ru.mpei.brics.extention.configirationClasses.NetworkElementConfiguration;
import ru.mpei.brics.extention.dto.DroolsFitnessDto;
import ru.mpei.brics.extention.helpers.DFHelper;


@Slf4j
public class AnalyzeMeasurements extends TickerBehaviour {

    ApplicationContext context = ApplicationContextHolder.getContext();
    private NetworkElementConfiguration cfg = ((NetworkElementAgent) myAgent).getCfg();
    double startTime;

    public AnalyzeMeasurements(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {
//        log.error("freq: {}", cfg.getF());
        if (cfg.getF() >= 50.1 || cfg.getF() <= 49.9) {
            this.stop();
            this.startTime = System.currentTimeMillis();
        }
    }

    @Override
    public int onEnd() {
//        log.error("BEHAVIOUR END");
        this.sendTradeRequestMsg();
        return 1;
    }


    private void sendTradeRequestMsg() {
        ACLMessage msg = new ACLMessage();
        msg.setPerformative(ACLMessage.REQUEST);
        msg.setProtocol("initiatePowerTrade");
        double fitnessVal = doRequestFitnessFromDrools();
        msg.setContent(Double.toString(fitnessVal));
        for(AID aid : DFHelper.findAgents(myAgent, "networkUnit")) {
            if(!aid.getLocalName().equals(myAgent.getLocalName())) {
                msg.addReceiver(aid);
            }
        }
        myAgent.send(msg);
        log.error("{} send fitness val: {};", myAgent.getLocalName(), msg.getContent());
    }

    private double doRequestFitnessFromDrools() {
//        double startTime = System.currentTimeMillis();
        KieContainer kieContainer = (KieContainer) context.getBean("kieContainer");
        KieSession kieSession = kieContainer.newKieSession();
        DroolsFitnessDto dto = new DroolsFitnessDto(myAgent.getLocalName(), cfg.getMaxP(), cfg.getCurrentP());
        kieSession.insert(dto);
        kieSession.fireAllRules();
//        log.error("Request start time: {}", (System.currentTimeMillis() - startTime) / 1000);
        return dto.getFitnessVal();
    }


}

