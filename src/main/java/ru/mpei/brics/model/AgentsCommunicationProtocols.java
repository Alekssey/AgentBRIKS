package ru.mpei.brics.model;

public enum AgentsCommunicationProtocols {
    FITNESS_TRADES("fitness trades"), NOTIFICATIONS("notifications");
    private String value;

    AgentsCommunicationProtocols(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
