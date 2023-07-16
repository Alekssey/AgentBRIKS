package ru.mpei.briks.appServiceLayer;

import jade.core.AID;
import jade.core.Agent;
import ru.mpei.briks.extention.dto.Measurement;

public interface ServiceInterface {
    String setPowerToGrid(double p, double q);
    void saveMeasurementInDB(Measurement m);
    Measurement getLastMeasurement();
    Agent getAgentFromContext(String agentName);

//    AID getAidFromContext(String agentName);
//    void saveFile(MultipartFile file);
//    String checkKZ(int begin, int end);
//    void setTriggerValue(double trigVal);

}
