package ru.mpei.brics.model;

import jade.core.AID;
import lombok.Data;
import ru.mpei.brics.services.MeasurementsUpdateService;
import ru.mpei.brics.utils.CommunicationWithContext;
import ru.mpei.brics.utils.knowledgeBase.DroolsCommunicator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Data
@XmlRootElement(name="cfg")
@XmlAccessorType(XmlAccessType.FIELD)
public class NetworkElementConfiguration {

    private MeasurementsUpdateService service;

    /** agent detector parameters*/
    @XmlElement(name = "interface")
    private String iFace;
    @XmlElement(name = "udpPeriod")
    private int udpMonitoringPeriod;
    @XmlElement(name = "udpPort")
    private int udpMonitoringPort;

    /** meta information */
    @XmlElement(name = "maxP")
    private double maxP;
    @XmlElement(name = "minP")
    private double minP;
    @XmlElement(name = "currentP")
    private double currentP;
    @XmlElement(name = "maxQ")
    private double maxQ;
    @XmlElement(name = "currentQ")
    private double currentQ;
    private double f = 50;
    @XmlElement
    private long behavioursPeriod;

    /** commands names */
    @XmlElement(name = "pMeasurementName")
    private String pMeasurementName;
    @XmlElement(name = "pCommandName")
    private String pCommandName;
    @XmlElement(name = "identifyingCommand")
    private String identifyingCommand;

    /** regulator parameters*/
    @XmlElement(name = "kp")
    private double kp;
    @XmlElement(name = "ki")
    private double ki;
    @XmlElement(name = "targetFreq")
    private double targetFreq;
    @XmlElement(name = "deltaFrequency")
    private double deltaFreq;
    @XmlElement
    private long maxRegulationTime;

    /** knowledge base components*/
    private DroolsCommunicator knowledgeBaseCommunicator;

    /** active power regulation */
    private Map<Double, AID> agentsQueue = new HashMap<>();

    /** additional buffer variables */
    private long regulationStartTime = 0;

    public NetworkElementConfiguration() {
        Optional<MeasurementsUpdateService> opService = CommunicationWithContext.getBean(MeasurementsUpdateService.class);
        if (opService.isPresent()) {
            this.service = opService.get();
            this.service.subscribe(this);
        }
    }

}
