package com.example.sportdepthchart.service;

import com.example.sportdepthchart.model.Player;
import com.example.sportdepthchart.model.PlayerPosition;
import com.example.sportdepthchart.model.PlayerPositionId;
import com.example.sportdepthchart.model.Position;
import com.example.sportdepthchart.repository.PlayerPositionRepository;
import com.example.sportdepthchart.repository.PlayerRepository;
import com.example.sportdepthchart.repository.PositionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class DepthChartServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private PlayerPositionRepository playerPositionRepository;

    private DepthChartService underTest;

    private Long sportId;
    private Long teamId;

    private Player player;
    private Position position;
    private PlayerPosition playerPosition;
    private String playerName;
    private String positionName;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new DepthChartService(playerRepository, positionRepository, playerPositionRepository);
        this.sportId = 1L;
        this.teamId = 1L;
        this.player = new Player(1L, 1L, "", LocalDateTime.now());
        this.position = new Position(1L, "", "", "", LocalDateTime.now());
        this.playerPosition = new PlayerPosition(new PlayerPositionId(1L, 1L), player, position, 1, LocalDateTime.now(),  LocalDateTime.now());
        this.playerName = "Evans, Mike";
        this.positionName = "LWR";
    }

    @Test
    void it_should_add_player_to_depthOne_when_depth_list_empty() {

        // given
        givenPlayerAndPositionExisted();
        given(playerPositionRepository.findPlayerPositionsByPositionIdOrderByPositionDepth(any())).willReturn(new ArrayList<>());

        // When
        underTest.addPlayerToDepthChart(sportId, teamId, positionName, playerName);

        // Then
        ArgumentCaptor<PlayerPosition> playerPositionArgumentCaptor =
                ArgumentCaptor.forClass(PlayerPosition.class);

        then(playerPositionRepository).should().save(playerPositionArgumentCaptor.capture());

        PlayerPosition playerPositionArgumentCaptorValue = playerPositionArgumentCaptor.getValue();

        assertThat(playerPositionArgumentCaptorValue.getPositionDepth()).isEqualTo(1);
    }

    @Test
    void it_should_add_player_lastDepth_when_depth_list_has_data() {

        // given
        List<PlayerPosition> list = getTwoPlayerPositions();

        // player, position exists
        givenPlayerAndPositionExisted();
        // depth list has 2 data
        given(playerPositionRepository.findPlayerPositionsByPositionIdOrderByPositionDepth(any())).willReturn(list);

        // When
        underTest.addPlayerToDepthChart(sportId, teamId, positionName, playerName);

        // Then
        ArgumentCaptor<PlayerPosition> playerPositionArgumentCaptor =
                ArgumentCaptor.forClass(PlayerPosition.class);

        then(playerPositionRepository).should().save(playerPositionArgumentCaptor.capture());

        PlayerPosition playerPositionArgumentCaptorValue = playerPositionArgumentCaptor.getValue();

        // so add player in depth 3
        assertThat(playerPositionArgumentCaptorValue.getPositionDepth()).isEqualTo(3);
    }

    @Test
    void it_should_add_player_in_particular_depth_when_input_has_positionDepth() {

        // given
        List<PlayerPosition> list = getTwoPlayerPositions();

        // player, position exists
        givenPlayerAndPositionExisted();
        // depth list has 2 data
        given(playerPositionRepository.findPlayerPositionsByPositionIdOrderByPositionDepth(any())).willReturn(list);

        // When
        underTest.addPlayerToDepthChart(sportId, teamId, positionName, playerName, 3);

        // Then
        ArgumentCaptor<PlayerPosition> playerPositionArgumentCaptor =
                ArgumentCaptor.forClass(PlayerPosition.class);

        then(playerPositionRepository).should().save(playerPositionArgumentCaptor.capture());

        PlayerPosition playerPositionArgumentCaptorValue = playerPositionArgumentCaptor.getValue();

        // so add player in depth 3
        assertThat(playerPositionArgumentCaptorValue.getPositionDepth()).isEqualTo(3);
    }

    @Test
    void it_should_throw_when_positionDepth_is_too_big() {

        // given
        List<PlayerPosition> list = getTwoPlayerPositions();

        // player, position exists
        givenPlayerAndPositionExisted();

        // depth list has 2 data
        given(playerPositionRepository.findPlayerPositionsByPositionIdOrderByPositionDepth(any())).willReturn(list);
        int currentDepth = list.size() + 1;

        // When
        // Then
        assertThatThrownBy(() -> underTest.addPlayerToDepthChart(sportId, teamId, positionName, playerName, 5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("position depth is too big, current legal depth upperbound is:" + currentDepth);
    }

    @Test
    void it_should_remove_player_successfully_return_one_removed_player() {

        // given
        // player, position exists
        givenPlayerAndPositionExisted();

        // depth list has one player
        given(playerPositionRepository.findById(any())).willReturn(Optional.of(this.playerPosition));

        // When
        List<String> result = underTest.removePlayerFromDepthChart(this.sportId, this.teamId, positionName, playerName);

        // Then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void it_should_remove_player_successfully_return_empty_result() {

        // given
        // player, position exists
        givenPlayerAndPositionExisted();

        // depth list has one player
        given(playerPositionRepository.findById(any())).willReturn(Optional.empty());

        // When
        List<String> result = underTest.removePlayerFromDepthChart(this.sportId, this.teamId, positionName, playerName);

        // Then
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    void it_should_getBackups_with_empty_list() {

        // given
        // player, position exists
        givenPlayerAndPositionExisted();

        // player has no backups
        given(playerPositionRepository.findById(any())).willReturn(Optional.empty());

        // When
        List<String> result = underTest.getBackups(this.sportId, this.teamId, positionName, playerName);

        // Then
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    void it_should_getBackups_successfully() {

        // given
        // player, position exists
        givenPlayerAndPositionExisted();

        // player has backups
        given(playerPositionRepository.findById(any())).willReturn(Optional.of(this.playerPosition));
        given(playerPositionRepository.findPlayerPositionsByPositionDepthGreaterThanAndPositionOrderByPositionDepth(this.playerPosition.getPositionDepth(), position))
                .willReturn(Arrays.asList(this.playerPosition, this.playerPosition));

        // When
        List<String> result = underTest.getBackups(this.sportId, this.teamId, positionName, playerName);

        // Then
        assertThat(result.size()).isGreaterThan(0);
    }

    @Test
    void it_should_getFullDepthChart_successfully() {

        // given
        given(positionRepository.findAll()).willReturn(Arrays.asList(this.position));

        // When
        List<String> result = underTest.getFullDepthChart();

        // Then
        assertThat(result.size()).isGreaterThan(0);
    }

    private void givenPlayerAndPositionExisted() {
        // player and position exists
        given(playerRepository.findByTeamIdAndPlayerName(teamId, playerName)).willReturn(Optional.of(this.player));
        given(positionRepository.findBySportIdAndPositionName(sportId, positionName)).willReturn(Optional.of(this.position));
    }

    // get ordered position depth PlayerPositions list which size is 2
    private List<PlayerPosition> getTwoPlayerPositions() {
        PlayerPosition playerPosition = new PlayerPosition(
                new PlayerPositionId(this.sportId, this.teamId),
                this.player,
                this.position,
                1, LocalDateTime.now(), null
        );
        PlayerPosition playerPosition2 = new PlayerPosition(
                new PlayerPositionId(this.sportId, this.teamId),
                this.player,
                this.position,
                2, LocalDateTime.now(), null
        );

        List<PlayerPosition> list = Arrays.asList(playerPosition, playerPosition2);
        return list;
    }
}