package ru.mpei.brics.agents;

import jade.core.Agent;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieSession;
import ru.mpei.brics.behaviours.networkElement.UpdateFrequencyData;
import ru.mpei.brics.behaviours.networkElement.AnalyzeFrequency;
import ru.mpei.brics.extention.configirationClasses.NetworkElementConfiguration;
import ru.mpei.brics.extention.helpers.AgentDetector.AgentDetector;
//import ru.mpei.brics.extention.helpers.DFHelper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

@Slf4j
public class NetworkElementAgent extends Agent {
    @Getter
    private NetworkElementConfiguration cfg = null;
    @Getter @Setter
    private KieSession kieSession = null;
    @Getter
    private AgentDetector aDetector = null;
//    private DFHelper df = new DFHelper();

    @Override
    protected void setup() {

//        df.registration(this, "networkUnit");
        log.info("{} was born", this.getLocalName());
        String configFileName = this.getLocalName() + "Configuration.xml";
        try{
            JAXBContext context = JAXBContext.newInstance(NetworkElementConfiguration.class);
            Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
            cfg = (NetworkElementConfiguration) jaxbUnmarshaller.unmarshal(new File("src/main/resources/agentsConfigs/" + configFileName));
        } catch (JAXBException e){
            e.printStackTrace();
        }

        System.out.println();
        this.aDetector = new AgentDetector(this.getAID(), "\\Device\\NPF_Loopback", this.cfg.getPeriod(), this.cfg.getPort());

        this.addBehaviour(new UpdateFrequencyData(this, 1000));
        this.addBehaviour(new AnalyzeFrequency(this, 1000));
//        this.addBehaviour(new ActivePowerImbalanceFSM(this, 1000));
//        this.addBehaviour(new AnalyzeFrequency(this, 1000));
//        this.addBehaviour(new ReceiveTradeRequest(this));
    }
}
