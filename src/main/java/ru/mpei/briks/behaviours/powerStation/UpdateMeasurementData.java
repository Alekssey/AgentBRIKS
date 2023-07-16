package ru.mpei.briks.behaviours.powerStation;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.briks.agents.NetworkElementAgent;
import ru.mpei.briks.appServiceLayer.ServiceInterface;
import ru.mpei.briks.extention.ApplicationContextHolder;
import ru.mpei.briks.extention.configirationClasses.StationConfiguration;
import ru.mpei.briks.extention.dto.AgentToGridDto;
import ru.mpei.briks.extention.dto.Measurement;
import ru.mpei.briks.extention.helpers.DFHelper;
import ru.mpei.briks.extention.helpers.JacksonHelper;

@Slf4j
public class UpdateMeasurementData extends TickerBehaviour {
    private ServiceInterface service = ApplicationContextHolder.getContext().getBean(ServiceInterface.class);
    private StationConfiguration cfg = ((NetworkElementAgent) myAgent).getCfg();


    public UpdateMeasurementData(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {
        Measurement m = service.getLastMeasurement();
//        log.info(JacksonHelper.toJackson(m));
        if (m != null) {
            cfg.setF(m.getFrequency());
        }

        sendMessageToGrid();
    }

    private void sendMessageToGrid() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent(JacksonHelper.toJackson(new AgentToGridDto(cfg.getCurrentGeneratingP())));
        msg.setProtocol("info about generation");
        msg.addReceiver(DFHelper.findAgents(myAgent, "grid").get(0));
        myAgent.send(msg);
    }
}
