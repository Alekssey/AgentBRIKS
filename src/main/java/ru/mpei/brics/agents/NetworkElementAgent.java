package ru.mpei.brics.agents;

import jade.core.Agent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.brics.behaviours.networkElement.ActivePowerImbalanceFSM;
import ru.mpei.brics.behaviours.networkElement.UpdateMeasurementData;
import ru.mpei.brics.extention.configirationClasses.NetworkElementConfiguration;
import ru.mpei.brics.extention.helpers.DFHelper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

@Slf4j
public class NetworkElementAgent extends Agent {
    @Getter
    private NetworkElementConfiguration cfg = null;
//    @Getter
//    private KieSession kieSession = null;
//    @Getter
//    private KieContainer kieContainer = null;
    private DFHelper df = new DFHelper();

    @Override
    protected void setup() {
        df.registration(this, "networkUnit");
        log.info("{} was born", this.getLocalName());
        String configFileName = this.getLocalName() + "Configuration.xml";
        try{
            JAXBContext context = JAXBContext.newInstance(NetworkElementConfiguration.class);
            Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
            cfg = (NetworkElementConfiguration) jaxbUnmarshaller.unmarshal(new File("src/main/resources/agentsConfigs/" + configFileName));
        } catch (JAXBException e){
            e.printStackTrace();
        }
        this.addBehaviour(new UpdateMeasurementData(this, 1000));
        this.addBehaviour(new ActivePowerImbalanceFSM(this, 1000));
//        this.addBehaviour(new AnalyzeFrequency(this, 1000));
//        this.addBehaviour(new ReceiveTradeRequest(this));
    }
}
