package ru.mpei.briks.behaviours.powerStation;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.briks.agents.GridAgent;
import ru.mpei.briks.agents.PowerStationAgent;
import ru.mpei.briks.appServiceLayer.ServiceInterface;
import ru.mpei.briks.extention.ApplicationContextHolder;
import ru.mpei.briks.extention.configirationClasses.StationConfiguration;
import ru.mpei.briks.extention.dto.AgentToGridDto;
import ru.mpei.briks.extention.dto.Measurement;
import ru.mpei.briks.extention.helpers.DFHelper;
import ru.mpei.briks.extention.helpers.JacksonHelper;

@Slf4j
public class AnalyzeMeasurements extends TickerBehaviour {

    private StationConfiguration cfg = ((PowerStationAgent) myAgent).getCfg();

    public AnalyzeMeasurements(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {
        log.info("Analyzing behaviour act");
        if (cfg.getF() >= 50.1 || cfg.getF() <= 49.9) {
            this.stop();
        }
    }

    @Override
    public int onEnd() {
        log.info("Analyzing behaviour end");
        myAgent.addBehaviour(new RegulateFrequency(myAgent, this.getPeriod()));
        return 1;
    }
}
