package ru.mpei.briks.behaviours.powerStation;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.briks.agents.PowerStationAgent;
import ru.mpei.briks.extention.configirationClasses.StationConfiguration;
import ru.mpei.briks.extention.dto.AgentToGridDto;
import ru.mpei.briks.extention.helpers.DFHelper;
import ru.mpei.briks.extention.helpers.JacksonHelper;

@Slf4j
public class RegulateFrequency extends TickerBehaviour {

    private StationConfiguration cfg = ((PowerStationAgent) myAgent).getCfg();
    private double kp = 1;
    private double ki = 0.5;
    double proportionalAdd = 0;
    double integralAdd = 0;

    public RegulateFrequency(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {
        log.info("Regulating behaviour act");
        double deltaF = 50 - cfg.getF();
        this.proportionalAdd = kp * deltaF;
        this.integralAdd += ki * deltaF;


        cfg.setCurrentGeneratingP(cfg.getCurrentGeneratingP() + this.proportionalAdd + this.integralAdd);
        log.info("PAdd:{}' IAdd: {}, SumP: {}", this.proportionalAdd, this.integralAdd, cfg.getCurrentGeneratingP());

        sendMessageToGrid();

        if(cfg.getF() >49.9 && cfg.getF() <= 50.1) {
            this.stop();
        }

    }

    private void sendMessageToGrid() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent(JacksonHelper.toJackson(new AgentToGridDto(cfg.getCurrentGeneratingP())));
        msg.setProtocol("info about generation");
        msg.addReceiver(DFHelper.findAgents(myAgent, "grid").get(0));
        myAgent.send(msg);
    }

    @Override
    public int onEnd() {
        log.info("Regulating behaviour end");
        myAgent.addBehaviour(new AnalyzeMeasurements(myAgent, this.getPeriod()));
        return 1;
    }
}
