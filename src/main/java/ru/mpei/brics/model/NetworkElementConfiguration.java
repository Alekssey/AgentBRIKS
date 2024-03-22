package ru.mpei.brics.model;

import jade.content.onto.annotations.Element;
import jade.core.AID;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.brics.services.MeasurementsUpdateService;
import ru.mpei.brics.utils.CommunicationWithContext;
import ru.mpei.brics.utils.knowledgeBase.DroolsCommunicator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@Data
@Slf4j
@XmlRootElement(name="cfg")
@XmlAccessorType(XmlAccessType.FIELD)
public class NetworkElementConfiguration {

    protected MeasurementsUpdateService service;

    /** agent detector parameters*/
    @XmlElement(name = "interface")
    protected String iFace;
    @XmlElement(name = "udpPeriod")
    protected int udpMonitoringPeriod;
    @XmlElement(name = "udpPort")
    protected int udpMonitoringPort;

    /** meta information */
//    @XmlElement
//    private ElementsTypes elementType;
    @XmlElement(name = "maxP")
    protected double maxP;
    @XmlElement(name = "minP")
    protected double minP;
    @XmlElement(name = "currentP")
    protected double currentP;
    @XmlElement(name = "maxQ")
    protected double maxQ;
    @XmlElement(name = "currentQ")
    protected double currentQ;
    protected double f = 50;
    @XmlElement
    protected long behavioursPeriod;

    /** commands names */
    @XmlElement(name = "pMeasurementName")
    protected String pMeasurementName;
    @XmlElement(name = "pCommandName")
    protected String pCommandName;
    @XmlElement(name = "identifyingCommand")
    protected String identifyingCommand;

    /** regulator parameters*/
    @XmlElement(name = "kp")
    protected double kp;
    @XmlElement(name = "ki")
    protected double ki;
    @XmlElement(name = "targetFreq")
    protected double targetFreq;
    @XmlElement(name = "deltaFrequency")
    protected double deltaFreq;
    @XmlElement
    protected long maxRegulationTime;

    /** knowledge base components*/
    protected DroolsCommunicator knowledgeBaseCommunicator;

    /** active power regulation */
    protected Map<Double, AID> agentsQueue = new HashMap<>();

    /** additional buffer variables */
    protected long regulationStartTime = 0;

    /** load additional fields*/
    @XmlElement(name = "loadStep")
    private List<Double> loadSteps;

    public NetworkElementConfiguration() {
        Optional<MeasurementsUpdateService> opService = CommunicationWithContext.getBean(MeasurementsUpdateService.class);
        if (opService.isPresent()) {
            this.service = opService.get();
            this.service.subscribe(this);
        }
    }

    public List<Double> getLoadSteps() {
        if (this.loadSteps != null) {
            return this.loadSteps;
        } else {
            return new ArrayList<>();
        }
    }
}
