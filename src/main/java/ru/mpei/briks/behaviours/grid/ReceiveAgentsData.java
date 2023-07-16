package ru.mpei.briks.behaviours.grid;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.briks.agents.GridAgent;
import ru.mpei.briks.extention.dto.AgentToGridDto;
import ru.mpei.briks.extention.helpers.JacksonHelper;

@Slf4j
public class ReceiveAgentsData extends Behaviour {
    private MessageTemplate mt = null;

    public ReceiveAgentsData(Agent a) {
        super(a);
    }

    @Override
    public void onStart() {
        this.mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            AgentToGridDto dto = JacksonHelper.fromJackson(msg.getContent(), AgentToGridDto.class);
            if (msg.getSender().getLocalName().contains("load")) {
                ((GridAgent) myAgent).cfg.getActivePowerData().put(msg.getSender(), - dto.getActivePower());
            } else {
                ((GridAgent) myAgent).cfg.getActivePowerData().put(msg.getSender(), dto.getActivePower());
            }
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
