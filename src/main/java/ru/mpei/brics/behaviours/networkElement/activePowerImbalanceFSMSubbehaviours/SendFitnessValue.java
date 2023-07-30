package ru.mpei.brics.behaviours.networkElement.activePowerImbalanceFSMSubbehaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
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

import java.util.List;

@Slf4j
public class SendFitnessValue extends OneShotBehaviour {
    ApplicationContext context = ApplicationContextHolder.getContext();
    private NetworkElementConfiguration cfg = ((NetworkElementAgent) myAgent).getCfg();

    public SendFitnessValue(Agent a) {
        super(a);
    }

    @Override
    public void action() {
        ACLMessage msg = new ACLMessage();

        msg.setPerformative(ACLMessage.REQUEST);
        msg.setProtocol("initiatePowerTrade");

        double fitnessVal = doRequestFitnessFromDrools();
        msg.setContent(Double.toString(fitnessVal));

        List<AID> activeAgents = DFHelper.findAgents(myAgent, "networkUnit");
        cfg.setNumberOfActiveAgents(activeAgents.size() - 1);
        for(AID aid : activeAgents) {
            if(!aid.getLocalName().equals(myAgent.getLocalName())) {
                msg.addReceiver(aid);
            }
        }

        myAgent.send(msg);
        log.info("{} send fitness val: {};", myAgent.getLocalName(), msg.getContent());
    }

    private double doRequestFitnessFromDrools() {
        KieContainer kieContainer = (KieContainer) context.getBean("kieContainer");
        KieSession kieSession = kieContainer.newKieSession();
        DroolsFitnessDto dto = new DroolsFitnessDto(myAgent.getLocalName(), cfg.getMaxP(), cfg.getCurrentP());
        kieSession.insert(dto);
        kieSession.fireAllRules();

        double fitnessVal = dto.getFitnessVal() + Math.random() * 0.0001;
        this.cfg.getFitnessValues().add(fitnessVal);
        this.cfg.getAgentsQueue().put(fitnessVal, myAgent.getAID());

        return fitnessVal;
    }
}
