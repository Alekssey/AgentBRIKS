package ru.mpei.brics.test.services;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DFHelper {
    private static final Logger log = LoggerFactory.getLogger(DFHelper.class);

    public void registration(Agent agent, String serviceName){  //Take agent to register and serviceName for it
        DFAgentDescription dfd = new DFAgentDescription();      // Create DF description variable
        dfd.setName(agent.getAID());                            // Add name to description
        ServiceDescription sd = new ServiceDescription();       // Create service description variable
        sd.setType(serviceName);                                // Add serviceName to service description
        sd.setName(agent.getLocalName());                       // Add name of agent to service description
        dfd.addServices(sd);                                    // Add service description to DG description
        try {
            DFService.register(agent, dfd);                     // Try to register agent
        } catch (FIPAException e) {
            e.printStackTrace();                                // Or throw Exception
        }
    }

    public static List<AID> findAgents(Agent a, String serviceName){
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(serviceName);
        dfd.addServices(sd);
        try {
            DFAgentDescription[] search = DFService.search(a, dfd);
            return Arrays.stream(search)
                    .map(DFAgentDescription::getName)
                    .collect(Collectors.toList());

        } catch (FIPAException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
