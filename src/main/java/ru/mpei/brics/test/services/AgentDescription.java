package ru.mpei.brics.test.services;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AgentDescription {
    private String agentName;
    private Class agentClass;
}
