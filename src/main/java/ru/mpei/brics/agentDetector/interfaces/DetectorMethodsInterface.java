package ru.mpei.brics.agentDetector.interfaces;

import jade.core.AID;

import java.util.List;

public interface DetectorMethodsInterface {
    void startDiscovering();
    void startSending();
    List<AID> getActiveAgents();
    void stopSending();
    void stopDiscovering();
}

