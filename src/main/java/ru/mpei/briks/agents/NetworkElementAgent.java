package ru.mpei.briks.agents;

import jade.core.Agent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieSession;
import ru.mpei.briks.behaviours.powerStation.AnalyzeMeasurements;
import ru.mpei.briks.behaviours.powerStation.UpdateMeasurementData;
import ru.mpei.briks.extention.configirationClasses.StationConfiguration;
import ru.mpei.briks.extention.helpers.DFHelper;
import ru.mpei.briks.extention.helpers.KieConfigurator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;

@Slf4j
public class NetworkElementAgent extends Agent {
    @Getter
    private StationConfiguration cfg = null;
    @Getter
    private KieSession kieSession = null;
    private DFHelper df = new DFHelper();

    @Override
    protected void setup() {
        df.registration(this, "station");
        log.info("{} was born", this.getLocalName());
        String configFileName = this.getLocalName() + "Configuration.xml";
        try{
            JAXBContext context = JAXBContext.newInstance(StationConfiguration.class);
            Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
            cfg = (StationConfiguration) jaxbUnmarshaller.unmarshal(new File("src/main/resources/agentsConfigs/" + configFileName));
        } catch (JAXBException e){
            e.printStackTrace();
        }

        KieConfigurator kieConfigurator = new KieConfigurator();
//        KieSession kieSession = null;src/main/resources/agentsConfigs/station1Configuration.xml
        try {
            this.kieSession = kieConfigurator.getKieSession("agentsRules/station1Rules.drl");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        this.addBehaviour(new UpdateMeasurementData(this, 1000));
        this.addBehaviour(new AnalyzeMeasurements(this, 1000));
//        this.addBehaviour(new TestSendBehaviour(this, 2000));

    }
}
