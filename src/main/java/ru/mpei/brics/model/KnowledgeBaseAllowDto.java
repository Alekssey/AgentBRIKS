package ru.mpei.brics.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KnowledgeBaseAllowDto {
    private double maxP;
    private double minP;
    private double currentP;
    private double currentFreq;
    private boolean allow;

    public KnowledgeBaseAllowDto(double maxP, double minP, double currentP, double currentFreq) {
        this.maxP = maxP;
        this.minP = minP;
        this.currentP = currentP;
        this.currentFreq = currentFreq;
    }

}
