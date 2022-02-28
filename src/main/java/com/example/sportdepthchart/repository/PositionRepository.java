package com.example.sportdepthchart.repository;

import com.example.sportdepthchart.model.Position;
import com.example.sportdepthchart.model.Sport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {

    Optional<Position> findBySportIdAndPositionName(Long sportId, String name);

}
