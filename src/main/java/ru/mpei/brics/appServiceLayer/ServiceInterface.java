package ru.mpei.brics.appServiceLayer;

import jade.core.Agent;
import ru.mpei.brics.extention.dto.Measurement;

public interface ServiceInterface {
    String setPowerToGrid(double p, double q);
    void saveMeasurementInDB(Measurement m);
    Measurement getLastMeasurement();
    Agent getAgentFromContext(String agentName);

    String setPowerToLoad(String loadName, double p, double q);

//    AID getAidFromContext(String agentName);
//    void saveFile(MultipartFile file);
//    String checkKZ(int begin, int end);
//    void setTriggerValue(double trigVal);

}
