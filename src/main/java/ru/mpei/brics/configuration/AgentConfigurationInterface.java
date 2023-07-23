package ru.mpei.brics.configuration;

import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import ru.mpei.brics.extention.agentDescription.AgentDescription;
import ru.mpei.brics.extention.agentDescription.AgentDescriptionContainer;

public interface AgentConfigurationInterface {
    AgentDescriptionContainer unmarshallConfig();
    AgentController createAgent (AgentDescription agentDescription, AgentContainer mainContainer);

}








//    AgentController createAgent ();
//    AgentController createAgent (AgentDescription agentDescription);
