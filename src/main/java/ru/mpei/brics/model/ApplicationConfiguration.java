package ru.mpei.brics.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
@XmlRootElement (name="agentDescriptions")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicationConfiguration {

    @XmlElement(name = "ip104Service")
    private String ip;

    @XmlElement(name = "port104Service")
    private String port;

    @XmlElement
    private long updateMeasurementPeriod;

    @XmlElement(name="agentDescription")
    private List<AgentDescription> agentDescriptionsList = new ArrayList<>();
}
