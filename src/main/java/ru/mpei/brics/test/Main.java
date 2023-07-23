package ru.mpei.brics.test;



import ru.mpei.brics.test.agents.ReceiverAgent;
import ru.mpei.brics.test.agents.SenderAgent;
import ru.mpei.brics.test.services.AgentDescription;
import ru.mpei.brics.test.services.AgentFactory;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<AgentDescription> agentDescriptionList = new ArrayList<>();
        agentDescriptionList.addAll(List.of(
                new AgentDescription("senderAgent", SenderAgent.class),
                new AgentDescription("receiverAgent", ReceiverAgent.class)
        ));
        AgentFactory.createAgents(agentDescriptionList);
    }
}
