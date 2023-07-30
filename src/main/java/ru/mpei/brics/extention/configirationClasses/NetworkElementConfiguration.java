package ru.mpei.brics.extention.configirationClasses;

import jade.core.AID;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@Data
@XmlRootElement(name="cfg")
@XmlAccessorType(XmlAccessType.FIELD)
public class NetworkElementConfiguration {
    /** meta information */
    @XmlElement(name="maxP")
    private double maxP;
    @XmlElement(name="currentP")
    private double currentP;
    @XmlElement(name="maxQ")
    private double maxQ;
    @XmlElement(name="currentQ")
    private double currentQ;
    private double f = 50;

    /** regulator coefficients*/
    @XmlElement(name = "kp")
    private double kp;
    @XmlElement(name = "ki")
    private double ki;

    /** active power trade */
    private int numberOfActiveAgents = 0;
    private boolean pTradeIsOpen = false;
    private ArrayList<Double> fitnessValues = new ArrayList<>();
    private Map<Double, AID> agentsQueue = new HashMap<>();


}
