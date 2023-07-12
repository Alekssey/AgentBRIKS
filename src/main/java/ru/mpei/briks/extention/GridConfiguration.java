package ru.mpei.briks.extention;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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
}
