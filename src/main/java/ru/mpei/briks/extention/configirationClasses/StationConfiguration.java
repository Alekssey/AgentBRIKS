package ru.mpei.briks.extention.configirationClasses;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name="cfg")
@XmlAccessorType(XmlAccessType.FIELD)
public class StationConfiguration {
    @XmlElement(name="maxP")
    private double maxP;
    @XmlElement(name="currentGeneratingP")
    private double currentGeneratingP;
    @XmlElement(name="maxQ")
    private double maxQ;
    @XmlElement(name="currentGeneratingQ")
    private double currentGeneratingQ;
    private double f;
}
