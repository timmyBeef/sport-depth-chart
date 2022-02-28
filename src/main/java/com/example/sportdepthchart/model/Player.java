package com.example.sportdepthchart.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Entity(name = "Player")
@Table(name = "player")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Long id;

    @Column(name = "player_no", nullable = false)
    private Long playerNo;

    @Column(name = "player_name", nullable = false)
    private String playerName;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    @UpdateTimestamp
    private LocalDateTime updatedAt;


    @ManyToOne
    @JoinColumn(
            name = "team_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "team_id")
    )
    private Team team;

    @OneToMany(
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            mappedBy = "player"
    )
    private List<PlayerPosition> playerPositions = new ArrayList<>();

    public void addPlayerPositions(PlayerPosition playerPosition) {
        if (!playerPositions.contains(playerPosition)) {
            playerPositions.add(playerPosition);
        }
    }

    public Player(Long id, Long playerNo, String playerName, LocalDateTime createdAt) {
        this.id = id;
        this.playerNo = playerNo;
        this.playerName = playerName;
        this.createdAt = createdAt;
    }
}
