package ru.mpei.brics.appServiceLayer;

import jade.core.Agent;
import ru.mpei.brics.extention.dto.Measurement;
import ru.mpei.brics.extention.dto.TSDBResponse;

import java.util.List;

public interface ServiceInterface {
    String setPowerToGrid(double p, double q);
    void saveMeasurementInDB(Measurement m);
    Measurement getLastMeasurement();
    Agent getAgentFromContext(String agentName);

    String setPowerToLoad(String loadName, double p, double q);
    TSDBResponse getMeasurementsByParameters(List<String> paramNames, int collectBeforeSec);

//    AID getAidFromContext(String agentName);
//    void saveFile(MultipartFile file);
//    String checkKZ(int begin, int end);
//    void setTriggerValue(double trigVal);

}
