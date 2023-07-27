package ru.mpei.brics.extention.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@AllArgsConstructor
@NoArgsConstructor
public class AgentToAgentDto {

    public AgentToAgentDto(TradeStatus status) {
        this.status = status;
    }

    private TradeStatus status;
}

