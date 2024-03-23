package ru.mpei.brics.model;

import lombok.Getter;

@Getter
public enum AgentsCommunicationProtocols {
    FITNESS_TRADES("fitness trades"), NOTIFICATIONS("notifications");
    private final String value;

    AgentsCommunicationProtocols(String value) {
        this.value = value;
    }

}
