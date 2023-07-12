package ru.mpei.briks.agents;

import jade.core.Agent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.briks.behaviours.grid.SpamMeasurement;
import ru.mpei.briks.extention.AgentDescriptionContainer;
import ru.mpei.briks.extention.GridConfiguration;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

@Slf4j
public class GridAgent extends Agent {
//    @Getter
    public GridConfiguration cfg = null;
    protected void setup() {
        log.info("{} was born", this.getLocalName());
//        log.info("{}", this);
        String configFileName = this.getLocalName() + "Configuration.xml";

        try{
            JAXBContext context = JAXBContext.newInstance(GridConfiguration.class);
            Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
            cfg = (GridConfiguration) jaxbUnmarshaller.unmarshal(new File("src/main/resources/" + configFileName));
        } catch (JAXBException e){
            e.printStackTrace();
        }
//        log.info("Agent instance: {}", this);

        this.addBehaviour(new SpamMeasurement(this, 2000, this.cfg));
    }

}
