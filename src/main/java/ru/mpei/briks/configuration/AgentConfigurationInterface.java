package ru.mpei.briks.configuration;

import jade.core.Agent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import ru.mpei.briks.extention.AgentDescription;
import ru.mpei.briks.extention.AgentDescriptionContainer;

public interface AgentConfigurationInterface {
    AgentDescriptionContainer unmarshallConfig();
    AgentController createAgent (AgentDescriptionContainer descriptionContainer, AgentContainer container);

}








//    AgentController createAgent ();
//    AgentController createAgent (AgentDescription agentDescription);
