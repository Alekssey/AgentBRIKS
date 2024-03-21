package ru.mpei.brics.beansConfiguration;

import ru.mpei.brics.agent.AgentsFabric;
import ru.mpei.brics.model.ApplicationConfiguration;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.mpei.brics.utils.CommunicatorWith104Service;
import ru.mpei.brics.utils.XmlSerialization;

@Slf4j
@Configuration("agentConfiguration")
public class BeansConfigurationImpl {
    @Value("${service.configuration.file.path}")
    private String configFilePath;

    @Bean
    public ApplicationConfiguration containerCfg() {
        return XmlSerialization.unMarshalAny(ApplicationConfiguration.class, configFilePath);
    }

    @Bean
    public AgentContainer jadeMainContainer() {
        ProfileImpl profile = new ProfileImpl();
        jade.core.Runtime.instance().setCloseVM(true);
        AgentContainer mainContainer = jade.core.Runtime.instance().createMainContainer(profile);

        try {
            mainContainer.start();
        } catch (ControllerException e) {
            e.printStackTrace();
            throw new RuntimeException("Can not start jade main container");
        }

        return mainContainer;
    }

    @Bean
    public CommunicatorWith104Service communicationWith104Service() {
        return new CommunicatorWith104Service();
    }
    @Bean
    public AgentsFabric agentsFabric() {
        return new AgentsFabric();
    }
}
