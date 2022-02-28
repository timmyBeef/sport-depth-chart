package com.example.sportdepthchart.repository;

import com.example.sportdepthchart.model.Player;
import com.example.sportdepthchart.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByTeamIdAndPlayerName(Long teamId, String name);
}
