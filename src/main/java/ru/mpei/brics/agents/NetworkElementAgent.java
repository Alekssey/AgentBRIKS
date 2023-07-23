package ru.mpei.brics.agents;

import jade.core.Agent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import ru.mpei.brics.behaviours.powerStation.AnalyzeMeasurements;
import ru.mpei.brics.behaviours.powerStation.ReceiveTradeRequest;
import ru.mpei.brics.behaviours.powerStation.UpdateMeasurementData;
import ru.mpei.brics.extention.configirationClasses.NetworkElementConfiguration;
import ru.mpei.brics.extention.helpers.DFHelper;
import ru.mpei.brics.extention.helpers.KieConfigurator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;

@Slf4j
public class NetworkElementAgent extends Agent {
    @Getter
    private NetworkElementConfiguration cfg = null;
    @Getter
    private KieSession kieSession = null;
    @Getter
    private KieContainer kieContainer = null;
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
        this.addBehaviour(new AnalyzeMeasurements(this, 1000));
        this.addBehaviour(new ReceiveTradeRequest(this));
//        this.addBehaviour(new TestSendBehaviour(this, 2000));
    }
}



//        String rulesFilePath = "agentsRules/" + this.getLocalName() + "Rules.drl";
//        try {
//            this.kieContainer = new KieConfigurator().getKieContainer(rulesFilePath);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

//        double fit = new AnalyzeMeasurements(this, 1000).doRequestFitnessFromDrools();
//        log.error("fitness: {}", fit);

//        log.error("Kie Container: {}", kieContainer);

//        log.error("{} way to drl: {}", this.getLocalName(), rulesFilePath);
//        KieConfigurator kieConfigurator = new KieConfigurator();
//        try {
//            this.kieSession = kieConfigurator.getKieSession(rulesFilePath);
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
