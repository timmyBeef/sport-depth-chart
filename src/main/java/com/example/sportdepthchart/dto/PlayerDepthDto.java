package com.example.sportdepthchart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerDepthDto {
    private Long playerNo;
    private String playerName;
    private int depth;
}
