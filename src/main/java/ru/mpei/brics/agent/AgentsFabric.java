package ru.mpei.brics.agent;

import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import org.springframework.beans.factory.annotation.Autowired;
import ru.mpei.brics.model.AgentDescription;
import ru.mpei.brics.model.ApplicationConfiguration;

import javax.annotation.PostConstruct;

public class AgentsFabric {

    @Autowired
    private ApplicationConfiguration agentsCfg;

    @Autowired
    private AgentContainer mainContainer;

    public AgentController createAgent(AgentDescription agentDescription) {

        AgentController newAgent = null;

        try {
            newAgent = mainContainer.createNewAgent(
                    agentDescription.getAgentName(),
                    agentDescription.getAgentClass().getName(),
                    agentDescription.getArgs());
            newAgent.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return newAgent;
    }

    @PostConstruct
    private void createAgents() {
        agentsCfg.getAgentDescriptionsList().forEach(ad -> this.createAgent(ad));
    }
}
