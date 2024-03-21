package ru.mpei.brics.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KnowledgeBaseFitnessDto {
    private double maxP;
    private double minP;
    private double currentP;
    private double currentFreq;
    private double fitnessVal;

    public KnowledgeBaseFitnessDto(double maxP, double minP, double currentP, double frequency) {
        this.maxP = maxP;
        this.minP = minP;
        this.currentP = currentP;
        this.currentFreq = frequency;
    }
}
