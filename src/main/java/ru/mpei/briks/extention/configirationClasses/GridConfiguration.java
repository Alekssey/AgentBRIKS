package ru.mpei.briks.extention.configirationClasses;

import jade.core.AID;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;

@Data
@XmlRootElement(name="cfg")
@XmlAccessorType(XmlAccessType.FIELD)
public class GridConfiguration {
    @XmlElement(name="f")
    private double f;
    @XmlElement(name="nP")
    private double necessaryP;
    @XmlElement(name="nQ")
    private double necessaryQ;
    @XmlElement(name="gP")
    private double generatedP;
    @XmlElement(name="gQ")
    private double generatedQ;

    private Map<AID, Double> activePowerData = new HashMap<>();

}
