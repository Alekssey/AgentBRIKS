package ru.mpei.briks.configuration;

import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.mpei.briks.extention.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

@Configuration("agentConfiguration")
@Slf4j
public class AgentsConfigurationImpl implements AgentConfigurationInterface {
    @Value("${agents.config.file.path}")
    private String configFilePath;
    private int index = 0;

    @Override
    @Bean("agentDescriptionsContainer")
    public AgentDescriptionContainer unmarshallConfig() {
        AgentDescriptionContainer adContainer = null;
        try{
            JAXBContext context = JAXBContext.newInstance(AgentDescriptionContainer.class);
            Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
            adContainer = (AgentDescriptionContainer) jaxbUnmarshaller.unmarshal(new File(configFilePath));
        } catch (JAXBException e){
            e.printStackTrace();
        }
        return adContainer;
    }

/** Самая актуальная версия*/
//    @Scope("prototype")
//    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Override
    @Bean("agentCreator")
    public AgentController createAgent(AgentDescriptionContainer descriptionContainer, AgentContainer mainContainer) {
        if (mainContainer == null) startJadeMainContainer();
        List<AgentDescription> descriptionsList = descriptionContainer.getAgentDescriptionList();
        int methodInternalIndex = this.index;
        if (this.index < descriptionsList.size() - 1) {
            this.index ++;
            createAgent(descriptionContainer, mainContainer);
        }

        AgentDescription agentDescription = descriptionsList.get(methodInternalIndex);
        AgentController newAgent = null;

        try {
            newAgent = mainContainer.createNewAgent(
                    agentDescription.getAgentName(),
                    agentDescription.getAgentClass().getName(),
                    agentDescription.getArgs());
            newAgent.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
        return newAgent;
    }


    @Bean("mainContainer")
    public AgentContainer startJadeMainContainer() {
        ProfileImpl profile = new ProfileImpl();
//        profile.setParameter("gui", "true");
        jade.core.Runtime.instance().setCloseVM(true);
        AgentContainer mainContainer = jade.core.Runtime.instance().createMainContainer(profile);

        try {
            mainContainer.start();
        } catch (ControllerException e) {
            e.printStackTrace();
        }

        return mainContainer;
    }

    /** песочница, Agent Description создается вручную, этот метод просто для более простого тестирования способов */
/*    @Override
    @Bean("agentCreator")
    public AgentController createAgent(AgentContainer container) {

        AgentDescription agentDescription = new AgentDescription();
        agentDescription.setAgentName("grid");
        agentDescription.setAgentClass(GridAgent.class);
        agentDescription.setArgs(new Object[]{"gridConfig.xml"});

        AgentController newAgent = null;

        try {
            newAgent = container.createNewAgent(
                    agentDescription.getAgentName(),
                    agentDescription.getAgentClass().getName(),
                    agentDescription.getArgs());
            newAgent.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }

//        GenericBeanDefinition gbd = new GenericBeanDefinition();
//        gbd.setBeanClass(AgentWrapper.class);
//        gbd.getPropertyValues().addPropertyValue("agentController", newAgent);
//        DefaultListableBeanFactory context = new DefaultListableBeanFactory();
//        context.registerBeanDefinition("gridBean", gbd);
//        context.getBean("gridBean");


//        GenericApplicationContext applicationContext = new GenericApplicationContext();
//        AgentController finalNewAgent = newAgent;
//        applicationContext.registerBean("grid", GridAgent.class);
//        applicationContext.refresh();


//        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
//        BeanDefinitionBuilder b = BeanDefinitionBuilder.rootBeanDefinition(AgentWrapper.class).addPropertyValue("agentController", newAgent);
//        beanFactory.registerBeanDefinition("gridBean", b.getBeanDefinition());

        return newAgent;
    }*/

    /** ТЕСТЫ - создание бинов одного тестового класса разного scope */
/*    @Bean("testSingletonBean")
    @Scope("singleton")
    public BeanTest createSingletonTestBean() {
        return new BeanTest();
    }

    @Bean("testPrototypeBean")
    @Scope("prototype")
    public BeanTest createPrototypeTestBean() {
        return new BeanTest();
    }*/


}
