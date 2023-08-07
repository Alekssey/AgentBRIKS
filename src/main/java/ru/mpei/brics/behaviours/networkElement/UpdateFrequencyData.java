package ru.mpei.brics.behaviours.networkElement;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.brics.agents.NetworkElementAgent;
import ru.mpei.brics.appServiceLayer.ServiceInterface;
import ru.mpei.brics.extention.ApplicationContextHolder;
import ru.mpei.brics.extention.configirationClasses.NetworkElementConfiguration;
import ru.mpei.brics.extention.dto.AgentToGridDto;
import ru.mpei.brics.extention.dto.Measurement;
import ru.mpei.brics.extention.dto.TSDBResponse;
import ru.mpei.brics.extention.helpers.DFHelper;
import ru.mpei.brics.extention.helpers.JacksonHelper;

import java.util.List;

@Slf4j
public class UpdateFrequencyData extends TickerBehaviour {
    private ServiceInterface service = ApplicationContextHolder.getContext().getBean(ServiceInterface.class);
    private NetworkElementConfiguration cfg = ((NetworkElementAgent) myAgent).getCfg();


    public UpdateFrequencyData(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {
/*        Measurement m = service.getLastMeasurement();
//        log.info(JacksonHelper.toJackson(m));
        if (m != null) {
            cfg.setF(m.getFrequency());
        }*/

        TSDBResponse response = service.getMeasurementsByParameters(List.of("frequency"), 0);
        if(response != null) {
            this.cfg.setF(Double.parseDouble(
                    response.getResponses().get(0).getValues().get(0)
            ));
        }

        sendMessageToGrid();
    }

    private void sendMessageToGrid() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent(JacksonHelper.toJackson(new AgentToGridDto(cfg.getCurrentP())));
        msg.setProtocol("info about generation");
//        List<AID> aidList = ((NetworkElementAgent) myAgent).getADetector().getActiveAgents();
//        for( AID aid : aidList) {
//            if(aid.getLocalName().equals("grid")) {
//                msg.addReceiver(aid);
//            }
//        }
        msg.addReceiver(DFHelper.findAgents(myAgent, "grid").get(0));
        myAgent.send(msg);
    }
}
