package com.example.sportdepthchart.repository;

import com.example.sportdepthchart.model.PlayerPosition;
import com.example.sportdepthchart.model.PlayerPositionId;
import com.example.sportdepthchart.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerPositionRepository extends JpaRepository<PlayerPosition, PlayerPositionId> {

    @Query(value = "select * from Player_Position where position_id = :positionId order by position_depth", nativeQuery = true)
    List<PlayerPosition> findPlayerPositionsByPositionIdOrderByPositionDepth(@Param("positionId") Long positionId);

    @Modifying
    @Query("update PlayerPosition ps set ps.positionDepth = ps.positionDepth + 1 where ps.positionDepth >= :positionDepth and ps.position.id = :positionId")
    void saveMoveDownBackupsDepth(@Param("positionDepth") int positionDepth, @Param("positionId") Long positionId);

    @Modifying
    @Query("update PlayerPosition ps set ps.positionDepth = ps.positionDepth - 1 where ps.positionDepth > :positionDepth and ps.position.id = :positionId")
    void saveMoveUpBackupsDepth(@Param("positionDepth") int positionDepth, @Param("positionId") Long positionId);

    List<PlayerPosition> findPlayerPositionsByPositionDepthGreaterThanAndPositionOrderByPositionDepth(int positionDepth, Position position);

}
