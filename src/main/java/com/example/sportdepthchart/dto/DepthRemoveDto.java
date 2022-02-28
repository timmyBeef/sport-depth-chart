package com.example.sportdepthchart.dto;

import lombok.Data;

@Data
public class DepthRemoveDto {
    private Long sportId;
    private Long teamId;
    private String positionName;
    private String playerName;
}
