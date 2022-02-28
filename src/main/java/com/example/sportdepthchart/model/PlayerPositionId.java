package com.example.sportdepthchart.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import java.io.Serializable;

@Setter
@Getter
@Embeddable
public class PlayerPositionId implements Serializable {

    @Column(name = "player_id")
    private Long playerId;

    @Column(name = "position_id")
    private Long positionId;

    public PlayerPositionId() {
    }

    public PlayerPositionId(Long playerId, Long positionId) {
        this.playerId = playerId;
        this.positionId = positionId;
    }
}
