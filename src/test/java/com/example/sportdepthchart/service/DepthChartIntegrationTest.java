package com.example.sportdepthchart.service;

import com.example.sportdepthchart.dto.CreateRequestDto;
import com.example.sportdepthchart.exception.DataNotExistException;
import com.example.sportdepthchart.model.Player;
import com.example.sportdepthchart.model.PlayerPosition;
import com.example.sportdepthchart.model.PlayerPositionId;
import com.example.sportdepthchart.model.Position;
import com.example.sportdepthchart.repository.PlayerPositionRepository;
import com.example.sportdepthchart.repository.PlayerRepository;
import com.example.sportdepthchart.repository.PositionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DepthChartIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private PlayerPositionRepository playerPositionRepository;

    private Long sportId = 1L;

    private Long teamId = 1L;

    @Test
    void it_should_add_player_to_depth_chart_last_depth() throws Exception {
        String playerName = "Howard, OJ";
        String positionName = "LWR";
        CreateRequestDto requestDto = CreateRequestDto.builder()
                .sportId(1L)
                .teamId(1L)
                .playerName(playerName)
                .positionName(positionName)
                .build();

        // add player
        ResultActions actions = mockMvc.perform(post("/api/v1/depth-chart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(requestDto))));
        actions.andExpect(status().isCreated());


        Player player = playerRepository.findByTeamIdAndPlayerName(teamId, playerName)
                .orElseThrow(() -> new DataNotExistException("can't find this player name in player table"));
        Position position = positionRepository.findBySportIdAndPositionName(sportId, positionName)
                .orElseThrow(() -> new DataNotExistException("can't find this positionName in positionName table"));

        Optional<PlayerPosition> playerPosition = playerPositionRepository.findById(new PlayerPositionId(player.getId(), position.getId()));
        List<PlayerPosition> after = playerPositionRepository.findPlayerPositionsByPositionIdOrderByPositionDepth(position.getId());

        assertThat(playerPosition).isPresent();
        assertThat(playerPosition.get().getPositionDepth()).isEqualTo(after.size());
    }

    /*
        The added player would get priority.
        Anyone below that player in the depth chart would get moved down(position depth number increasing) a position_depth
     */
    @Test
    void it_should_add_player_to_depth_chart_particular_depth_and_others_moved_down() throws Exception {
        String playerName = "Howard, OJ";
        String positionName = "LWR";
        CreateRequestDto requestDto = CreateRequestDto.builder()
                .sportId(1L)
                .teamId(1L)
                .playerName(playerName)
                .positionName(positionName)
                .positionDepth(1) // add player with position depth 1
                .build();

        Position position = positionRepository.findBySportIdAndPositionName(sportId, positionName)
                .orElseThrow(() -> new DataNotExistException("can't find this positionName in positionName table"));
        List<PlayerPosition> before = playerPositionRepository.findPlayerPositionsByPositionIdOrderByPositionDepth(position.getId());

        // add player
        ResultActions actions = mockMvc.perform(post("/api/v1/depth-chart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(requestDto))));
        actions.andExpect(status().isCreated());


        Player player = playerRepository.findByTeamIdAndPlayerName(teamId, playerName)
                .orElseThrow(() -> new DataNotExistException("can't find this player name in player table"));

        Optional<PlayerPosition> playerPosition = playerPositionRepository.findById(new PlayerPositionId(player.getId(), position.getId()));
        List<PlayerPosition> after = playerPositionRepository.findPlayerPositionsByPositionIdOrderByPositionDepth(position.getId());

        assertThat(playerPosition).isPresent();
        // check the added player's depth is 1
        assertThat(playerPosition.get().getPositionDepth()).isEqualTo(1);

        // compare origin player's depth number + 1 == new depth number
        // (comparing exclude the first one (after list), first one is newly added player)
        for (int i = 1; i < after.size(); i++) {
            int positionDepth = before.get(i - 1).getPositionDepth();
            int expectedPositionDepth = after.get(i).getPositionDepth();
            assertThat(positionDepth + 1).isEqualTo(expectedPositionDepth);
        }
    }

    @Test
    void it_should_remove_player_from_depth_chart_particular_depth_and_others_moved_up() throws Exception {
        String playerName = "Howard, OJ";
        String positionName = "LWR";
        CreateRequestDto requestDto = CreateRequestDto.builder()
                .sportId(1L)
                .teamId(1L)
                .playerName(playerName)
                .positionName(positionName)
                .build();

        Position position = positionRepository.findBySportIdAndPositionName(sportId, positionName)
                .orElseThrow(() -> new DataNotExistException("can't find this positionName in positionName table"));
        List<PlayerPosition> before = playerPositionRepository.findPlayerPositionsByPositionIdOrderByPositionDepth(position.getId());

        // remove player
        ResultActions actions = mockMvc.perform(delete("/api/v1/depth-chart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(requestDto))));
        actions.andExpect(status().isOk());

        List<PlayerPosition> after = playerPositionRepository.findPlayerPositionsByPositionIdOrderByPositionDepth(position.getId());

        // after delete, check the depth list length is less than before by one.
        assertThat(before.size() - 1).isEqualTo(after.size());

        // after delete, check the position depth moved up
        for (int i = 1; i < before.size(); i++) {
            int positionDepth = before.get(i).getPositionDepth();
            int expectedPositionDepth = after.get(i-1).getPositionDepth();
            assertThat(positionDepth - 1).isEqualTo(expectedPositionDepth);
        }
    }

    @Test
    void it_should_get_backups() throws Exception {
        String playerName = "Evans, Mike";
        String positionName = "LWR";
        CreateRequestDto requestDto = CreateRequestDto.builder()
                .sportId(1L)
                .teamId(1L)
                .playerName(playerName)
                .positionName(positionName)
                .build();

        // get backups
        ResultActions actions = mockMvc.perform(get("/api/v1/depth-chart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(requestDto))));
        actions.andExpect(status().isOk());

        MvcResult mvcResult = actions.andReturn();
        String content = mvcResult.getResponse().getContentAsString();

        List<String> list = objectToJson(content);
        List<PlayerPosition> getBackups = findBackups(positionName, playerName);

        // compare the result is the same
        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i);
            String t = toResultString(getBackups.get(i));
            assertThat(s).isEqualTo(t);
        }
    }

    @Test
    void it_should_get_full_depthChart() throws Exception {

        // getFullDepthChart
        ResultActions actions = mockMvc.perform(get("/api/v1/depth-chart/all")
                .contentType(MediaType.APPLICATION_JSON));
        actions.andExpect(status().isOk());

        MvcResult mvcResult = actions.andReturn();
        String content = mvcResult.getResponse().getContentAsString();

        List<String> list = objectToJson(content);
        assertThat(list.size()).isGreaterThan(0);
    }

    private String objectToJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            fail("Failed to convert object to json");
            return null;
        }
    }

    private List<String> objectToJson(String jsonString) {
        try {
            return new ObjectMapper().readValue(jsonString, new TypeReference<List<String>>(){});
        } catch (JsonProcessingException e) {
            fail("Failed to convert object to json");
            return null;
        }
    }

    private List<PlayerPosition> findBackups(String positionName, String playerName) {
        Position position = positionRepository.findBySportIdAndPositionName(sportId, positionName)
                .orElseThrow(() -> new DataNotExistException("can't find this positionName in positionName table"));

        Player player = playerRepository.findByTeamIdAndPlayerName(teamId, playerName)
                .orElseThrow(() -> new DataNotExistException("can't find this player name in player table"));

        Optional<PlayerPosition> playerPosition = playerPositionRepository.findById(new PlayerPositionId(player.getId(), position.getId()));
        PlayerPosition curr = playerPosition.orElseGet(() -> new PlayerPosition());

        List<PlayerPosition> list = playerPositionRepository.findPlayerPositionsByPositionIdOrderByPositionDepth(position.getId());
        return list.stream().filter(v -> v.getPositionDepth() > curr.getPositionDepth())
                .collect(Collectors.toList());
    }

    private String toResultString(PlayerPosition ps) {
        return "#" + ps.getPlayer().getPlayerNo() + " - " + ps.getPlayer().getPlayerName();
    }
}