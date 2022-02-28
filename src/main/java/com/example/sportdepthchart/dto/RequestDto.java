package com.example.sportdepthchart.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestDto {
    private Long sportId;
    private Long teamId;
    private String positionName;
    private String playerName;
}
