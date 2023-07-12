package ru.mpei.briks.appServiceLayer;

import jade.core.AID;
import jade.core.Agent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import ru.mpei.briks.agents.GridAgent;
import ru.mpei.briks.extention.ApplicationContextHolder;
import ru.mpei.briks.extention.GridConfiguration;

import java.lang.reflect.Field;
import java.util.List;


@Slf4j
@Service("service")
public class ServiceImpl implements ServiceInterface {

    private ClassPathXmlApplicationContext context;

//    @Autowired
//    private RepositoryInterface repository;

    @Override
    public String setPowerToGrid(double p, double q) {
        log.info("Received data - Active power: {}; Reactive power: {}", p, q);
        GridAgent gridAgent = (GridAgent) extractAgentFromContext("grid");
//        log.info("extracted agent: {}", gridAgent);
        gridAgent.cfg.setNecessaryP(p);
        gridAgent.cfg.setNecessaryQ(q);

        return "Values successfully changed";
    }



/*    public Agent extractAgentFromContext(String agentName) {
        ApplicationContext context = ApplicationContextHolder.getContext();
        AgentContainer mainContainer = (AgentContainer) context.getBean("mainContainer");
        Agent a = null;

        try {
            AgentController agentController = mainContainer.getAgent(agentName);

            try {
                Field agentAidField = agentController.getClass().getDeclaredField("agentID");
                agentAidField.setAccessible(true);

                Field agentContainerField = agentController.getClass().getDeclaredField("myContainer");
                agentContainerField.setAccessible(true);

                AID agentAID = (AID) agentAidField.get(agentController);
                jade.core.AgentContainer agentContainer = (jade.core.AgentContainer) agentContainerField.get(agentController);

                a = agentContainer.acquireLocalAgent(agentAID);

            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } catch (ControllerException e) {
            throw new RuntimeException(e);
        }
        return a;
    }*/

    public Agent extractAgentFromContext(String agentName) {
        ApplicationContext context = ApplicationContextHolder.getContext();
        AgentContainer mainContainer = (AgentContainer) context.getBean("mainContainer");

        Agent agentInstance = null;

        try {
            AgentController agentController = mainContainer.getAgent(agentName);

            Field agentAidField = agentController.getClass().getDeclaredField("agentID");
            agentAidField.setAccessible(true);
            Field agentContainerField = agentController.getClass().getDeclaredField("myContainer");
            agentContainerField.setAccessible(true);

            AID agentAID = (AID) agentAidField.get(agentController);
            jade.core.AgentContainer agentContainer = (jade.core.AgentContainer) agentContainerField.get(agentController);

            agentInstance = agentContainer.acquireLocalAgent(agentAID);
        } catch (ControllerException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return agentInstance;
    }







}
