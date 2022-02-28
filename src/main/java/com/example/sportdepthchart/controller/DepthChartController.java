package com.example.sportdepthchart.controller;

import com.example.sportdepthchart.dto.CreateRequestDto;
import com.example.sportdepthchart.dto.RequestDto;
import com.example.sportdepthchart.service.DepthChartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="Sport Depth Chart API")
@RestController
@RequestMapping("api/v1/depth-chart")
@RequiredArgsConstructor
public class DepthChartController {

    private final DepthChartService service;

    @Operation(summary = "add player to depth chart")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addPlayerToDepthChart(@RequestBody CreateRequestDto dto) {
        if (dto.getPositionDepth() == null) {
            service.addPlayerToDepthChart(dto.getSportId(), dto.getTeamId(), dto.getPositionName(), dto.getPlayerName());
        } else {
            service.addPlayerToDepthChart(dto.getSportId(), dto.getTeamId(), dto.getPositionName(), dto.getPlayerName(), dto.getPositionDepth());
        }
    }

    @Operation(summary = "remove player from depth chart")
    @DeleteMapping
    public List<String> removePlayerFromDepthChart(@RequestBody RequestDto dto) {
        return service.removePlayerFromDepthChart(dto.getSportId(), dto.getTeamId(), dto.getPositionName(), dto.getPlayerName());
    }

    @Operation(summary = "get backups")
    @GetMapping
    public List<String> getBackups(@RequestBody RequestDto dto) {
        return service.getBackups(dto.getSportId(), dto.getTeamId(), dto.getPositionName(), dto.getPlayerName());
    }

    @Operation(summary = "get full depth chart")
    @GetMapping("/all")
    public List<String> getFullDepthChart() {
        return service.getFullDepthChart();
    }
}
