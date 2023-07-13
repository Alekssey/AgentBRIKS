package ru.mpei.briks.agents;

import jade.core.Agent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.briks.behaviours.powerStation.AnalyzeMeasurements;
import ru.mpei.briks.behaviours.powerStation.UpdateMeasurementData;
import ru.mpei.briks.extention.configirationClasses.StationConfiguration;
import ru.mpei.briks.extention.helpers.DFHelper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

@Slf4j
public class PowerStationAgent extends Agent {
    @Getter
    private StationConfiguration cfg = null;
    private DFHelper df = new DFHelper();

    @Override
    protected void setup() {
        df.registration(this, "station");
        log.info("{} was born", this.getLocalName());
        String configFileName = this.getLocalName() + "Configuration.xml";
        try{
            JAXBContext context = JAXBContext.newInstance(StationConfiguration.class);
            Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
            cfg = (StationConfiguration) jaxbUnmarshaller.unmarshal(new File("src/main/resources/" + configFileName));
        } catch (JAXBException e){
            e.printStackTrace();
        }

        this.addBehaviour(new UpdateMeasurementData(this, 500));
        this.addBehaviour(new AnalyzeMeasurements(this, 1000));
//        this.addBehaviour(new TestSendBehaviour(this, 2000));

    }
}
