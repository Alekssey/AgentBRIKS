package ru.mpei.brics.model;

import jade.core.AID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AIDWrapper {

    private String aidName;
    private String aidHap;
    private String[] aidAddressesArray;

    public AIDWrapper(AID myAgent) {
        this.aidName = myAgent.getName();
        this.aidHap = myAgent.getHap();
        this.aidAddressesArray = myAgent.getAddressesArray();
    }

    public AID createAid() {
        AID aid = new AID(aidName, true);
        if (aidAddressesArray != null) {
            for (String addresses : aidAddressesArray) {
                aid.addAddresses(addresses);
            }
        }
        return aid;
    }
}
