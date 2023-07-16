package ru.mpei.briks.appServiceLayer;

import jade.core.AID;
import jade.core.Agent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import ru.mpei.briks.agents.GridAgent;
import ru.mpei.briks.appRepositoryLayer.RepositoryInterface;
import ru.mpei.briks.extention.ApplicationContextHolder;
import ru.mpei.briks.extention.dto.Measurement;

import java.lang.reflect.Field;
import java.util.List;


@Slf4j
@Service("service")
public class ServiceImpl implements ServiceInterface {

    @Autowired
    private RepositoryInterface repository;
    private int lastIndex = 0;
    private GridAgent gridAgent = null;

    @Override
    public String setPowerToGrid(double p, double q) {
        if (this.gridAgent == null) {
            this.gridAgent = (GridAgent) getAgentFromContext("grid");
        }
        gridAgent.cfg.setNecessaryP(p);
        gridAgent.cfg.setNecessaryQ(q);
        return "Values successfully changed";
    }

    @Override
    public void saveMeasurementInDB(Measurement m) {
        this.lastIndex = (int) m.getId();
        repository.save(m);
    }

    @Override
    public Measurement getLastMeasurement() {
        List<Measurement> mList = repository.getMeasurements(lastIndex, lastIndex);
        if (mList.size() != 0) {
            return mList.get(0);
        }
        return null;
    }

    @Override
    public Agent getAgentFromContext(String agentName) {
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
            agentContainer.releaseLocalAgent(agentAID);

        } catch (ControllerException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return agentInstance;
    }








}
