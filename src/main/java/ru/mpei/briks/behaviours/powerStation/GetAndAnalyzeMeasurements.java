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
import ru.mpei.briks.extention.dto.AgentToGridDto;
import ru.mpei.briks.extention.dto.Measurement;
import ru.mpei.briks.extention.helpers.DFHelper;
import ru.mpei.briks.extention.helpers.JacksonHelper;

@Slf4j
public class GetAndAnalyzeMeasurements extends TickerBehaviour {

    private ServiceInterface service = ApplicationContextHolder.getContext().getBean(ServiceInterface.class);
    private GridAgent grid = null;
    private DFHelper df = new DFHelper();

    public GetAndAnalyzeMeasurements(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {
        Measurement m = service.getLastMeasurement();
        log.info("{} get and analyze measurements : {}", myAgent.getLocalName(), m);

//        if (this.grid != null) {
//            log.info("GRID IS NULL");
//            sendMessageToGrid();
//        } else {
//            service.getAgentFromContext("grid");
//            log.info("SEND MESSAGE");
//        }

        sendMessageToGrid();

    }

    private void sendMessageToGrid() {
        AID gridAid = DFHelper.findAgents(myAgent, "grid").get(0);
        double generatingP = ((PowerStationAgent) myAgent).getCfg().getCurrentGeneratingP();
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent(JacksonHelper.toJackson(new AgentToGridDto(generatingP)));
        msg.setProtocol("info about generation");
        msg.addReceiver(gridAid);
        myAgent.send(msg);
    }
}
