package mas.services;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="cfg")
@Getter @Setter
public class CfgClass {
    @XmlElement
    private String droolsFileName;
    @XmlElement
    private double maxPower;
    @XmlElement
    private List<Integer> controlBus;

    private double frequency = 50;
    private double availablePowerPerCent = 100;
    private double generatingPower = 0;
//    private KieSession kieSession;
    private List<Double> busVoltagesList = new ArrayList<>();

    public CfgClass() {
        for (int i = 0; i < 20; i++) {
            this.busVoltagesList.add(null);
        }
    }
}
