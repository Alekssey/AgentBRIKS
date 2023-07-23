package ru.mpei.brics.extention.configirationClasses;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name="cfg")
@XmlAccessorType(XmlAccessType.FIELD)
public class NetworkElementConfiguration {
    @XmlElement(name="maxP")
    private double maxP;
    @XmlElement(name="currentP")
    private double currentP;
    @XmlElement(name="maxQ")
    private double maxQ;
    @XmlElement(name="currentQ")
    private double currentQ;
    private double f = 50;
    private boolean pTradeIsOpen = false;
}
