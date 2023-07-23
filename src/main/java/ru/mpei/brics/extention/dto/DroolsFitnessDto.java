package ru.mpei.brics.extention.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DroolsFitnessDto {

    public DroolsFitnessDto(String agent, double maxP, double currentP) {
        this.agentName = agent;
        this.maxP = maxP;
        this.currentP = currentP;
    }

    private String agentName;
    private double maxP;
    private double currentP;
    private double fitnessVal;
}
