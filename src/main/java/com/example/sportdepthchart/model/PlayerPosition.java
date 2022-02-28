package com.example.sportdepthchart.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "PlayerPosition")
@Table(name = "player_position")
public class PlayerPosition {

    @EmbeddedId
    private PlayerPositionId id;

    @ManyToOne
    @MapsId("playerId")
    @JoinColumn(
            name = "player_id",
            foreignKey = @ForeignKey(
                    name = "player_id_fk"
            )
    )
    private Player player;

    @ManyToOne
    @MapsId("positionId")
    @JoinColumn(
            name = "position_id",
            foreignKey = @ForeignKey(
                    name = "position_id_fk"
            )
    )
    private Position position;

    @Column(name = "position_depth", nullable = false)
    private int positionDepth;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
