package com.example.sportdepthchart.service;

import com.example.sportdepthchart.dto.PlayerDepthDto;
import com.example.sportdepthchart.exception.DataNotExistException;
import com.example.sportdepthchart.model.*;
import com.example.sportdepthchart.repository.PlayerPositionRepository;
import com.example.sportdepthchart.repository.PlayerRepository;
import com.example.sportdepthchart.repository.PositionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class DepthChartService {

    private final PlayerRepository playerRepository;

    private final PositionRepository positionRepository;

    private final PlayerPositionRepository playerPositionRepository;

    public DepthChartService(PlayerRepository playerRepository, PositionRepository positionRepository, PlayerPositionRepository playerPositionRepository) {
        this.playerRepository = playerRepository;
        this.positionRepository = positionRepository;
        this.playerPositionRepository = playerPositionRepository;
    }

    /*
        Adds a player to the depth chart at a given position
        Adding a player without a position_depth would add them to the end of the depth chart at that position
     */
    @Transactional(rollbackFor = Exception.class)
    public void addPlayerToDepthChart(Long sportId, Long teamId, String position, String playerName) {
        Player player = playerRepository.findByTeamIdAndPlayerName(teamId, playerName)
                .orElseThrow(() -> new DataNotExistException("can't find this player name in player table, please insert player info first"));

        Position pos = positionRepository.findBySportIdAndPositionName(sportId, position)
                .orElseThrow(() -> new DataNotExistException("can't find this position in position table, please insert position info first"));

        // if player in this position is existed, remove first
        this.removePlayerFromDepthChart(sportId, teamId, position, playerName);

        log.info("pos.getId(): {}", pos.getId());
        // if this position's player depth list is empty, add this player's depth with 1, or the depth is the max depth + 1
        int depth = getCurrentDepth(pos);

        log.info("player:" + player.getId());
        log.info("pos:" + pos.getId());
        log.info("depth:" + depth);

        PlayerPosition ps = setPlayerPosition(player, pos, depth);
        playerPositionRepository.save(ps);
    }

    /*
     input with positionDepth
     The added player would get priority. Anyone below that player in the depth chart would get moved down a position_depth
     */
    @Transactional(rollbackFor = Exception.class)
    public void addPlayerToDepthChart(Long sportId, Long teamId, String positionName, String playerName, int positionDepth) {
        Player player = playerRepository.findByTeamIdAndPlayerName(teamId, playerName)
                .orElseThrow(() -> new DataNotExistException("can't find this player name in player table"));

        Position position = positionRepository.findBySportIdAndPositionName(sportId, positionName)
                .orElseThrow(() -> new DataNotExistException("can't find this positionName in positionName table"));

        int currentDepth = getCurrentDepth(position);
        if (positionDepth > currentDepth) {
            throw new IllegalArgumentException("position depth is too big, current legal depth upperbound is:" + currentDepth);
        }

        // if player in this positionName is existed, remove first
        this.removePlayerFromDepthChart(sportId, teamId, positionName, playerName);

        playerPositionRepository.saveMoveDownBackupsDepth(positionDepth, position.getId());
        PlayerPosition ps = setPlayerPosition(player, position, positionDepth);
        playerPositionRepository.save(ps);
    }

    /*
        Removes a player from the depth chart for a given position and returns that player
        (the backups of this player's depth should move up)

        An empty list should be returned if that player is not listed in the depth chart at that position
    */
    @Transactional(rollbackFor = Exception.class)
    public List<String> removePlayerFromDepthChart(Long sportId, Long teamId, String position, String playerName) {
        List<String> result = new ArrayList<>();
        Player player = playerRepository.findByTeamIdAndPlayerName(teamId, playerName)
                .orElseThrow(() -> new DataNotExistException("can't find this player name in player table"));

        Position pos = positionRepository.findBySportIdAndPositionName(sportId, position)
                .orElseThrow(() -> new DataNotExistException("can't find this position in position table"));

        Optional<PlayerPosition> playerPosition = playerPositionRepository.findById(new PlayerPositionId(player.getId(), pos.getId()));
        if (!playerPosition.isPresent()) {
            return result;
        }
        PlayerPosition removeTarget = playerPosition.get();
        playerPositionRepository.saveMoveUpBackupsDepth(removeTarget.getPositionDepth(), pos.getId());
        playerPositionRepository.delete(removeTarget);

        return Arrays.asList(toResultString(removeTarget.getPlayer()));
    }

    /*
        For a given player and position, we want to see all players that are “Backups”, those with a lower position_depth
        An empty list should be returned if the given player has no Backups (empty list)
        An empty list should be returned if the given player is not listed in the depth chart at that position (not in position, return empty list)
     */
    public List<String> getBackups(Long sportId, Long teamId, String positionName, String playerName) {
        List<String> result = new ArrayList<>();

        Player player = playerRepository.findByTeamIdAndPlayerName(teamId, playerName)
                .orElseThrow(() -> new DataNotExistException("can't find this player name in player table"));

        Position position = positionRepository.findBySportIdAndPositionName(sportId, positionName)
                .orElseThrow(() -> new DataNotExistException("can't find this position in position table"));

        int playerDepth = playerPositionRepository.findById(new PlayerPositionId(player.getId(), position.getId()))
                .map(v -> v.getPositionDepth()) //int
                .orElse(-1);

        if (playerDepth == -1) {
            return result;
        }

        List<PlayerPosition> list =
                playerPositionRepository.findPlayerPositionsByPositionDepthGreaterThanAndPositionOrderByPositionDepth(playerDepth, position);

        for (PlayerPosition ps : list) {
            result.add(toResultString(ps));
        }
        return result;
    }

    public List<String> getFullDepthChart() {
        List<Position> allPosition = positionRepository.findAll();

        TreeMap<Long, List<PlayerDepthDto>> map = new TreeMap<>();
        Map<Long, Position> positionNameMap = new HashMap<>();

        for (Position position : allPosition) {
            Long positionId = position.getId();
            String positionName = position.getPositionName();
            positionNameMap.put(positionId, position);
            List<PlayerPosition> all = position.getPlayerPositions();

            map.putIfAbsent(positionId, new ArrayList<>());
            for (PlayerPosition data : all) {
                Player player = data.getPlayer();
                map.get(positionId).add(new PlayerDepthDto(
                        player.getPlayerNo(),
                        player.getPlayerName(),
                        data.getPositionDepth())
                );
            }
        }

        return toResultListString(map, positionNameMap);
    }

    private Long positionId(Position position) {
        return position.getId();
    }

    String toResultString(Player player) {
        return "#" + player.getPlayerNo() + " - " + player.getPlayerName();
    }

    String toResultString(PlayerPosition ps) {
        return "#" + ps.getPlayer().getPlayerNo() + " - " + ps.getPlayer().getPlayerName();
    }

    List<String> toResultListString(TreeMap<Long, List<PlayerDepthDto>> map, Map<Long, Position> positionNameMap) {
        List<String> result = new ArrayList<>();
        StringBuilder sb;
        for (Long positionId : map.keySet()) {
            sb = new StringBuilder();
            List<PlayerDepthDto> list = map.get(positionId);
            Collections.sort(list, (a, b) -> a.getDepth() - b.getDepth());

            String realPositionName = positionNameMap.get(positionId).getRealPositionName();
            sb.append(realPositionName).append("-");

            if (list.size() > 0) {
                for (PlayerDepthDto dto : list) {
                    sb.append("(#")
                            .append(dto.getPlayerNo())
                            .append(", ")
                            .append(dto.getPlayerName())
                            .append("),");
                }
                sb.deleteCharAt(sb.length() - 1);
            }

            result.add(sb.toString());
        }
        return result;
    }


    protected int getCurrentDepth(Position pos) {
        int depth = 1;
        List<PlayerPosition> playerPositions = playerPositionRepository.findPlayerPositionsByPositionIdOrderByPositionDepth(pos.getId());

        if (playerPositions.size() != 0) {
            // or find max positionDepth value, then add new PlayerPosition with max positionDepth+1
            depth = playerPositions.stream()
                    .mapToInt(v -> v.getPositionDepth()).max().getAsInt() + 1;
        }
        return depth;
    }

    private PlayerPosition setPlayerPosition(Player player, Position pos, int depth) {
        PlayerPosition ps = new PlayerPosition();
        ps.setId(new PlayerPositionId(player.getId(), pos.getId()));
        ps.setPlayer(player);
        ps.setPosition(pos);
        ps.setPositionDepth(depth);
        ps.setCreatedAt(LocalDateTime.now());
        ps.setUpdatedAt(LocalDateTime.now());
        return ps;
    }
}
