package com.example.sportdepthchart.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Position")
@Table(name = "position")
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Long id;

    @Column(name = "position_name", nullable = false)
    private String positionName;

    @Column(name = "real_position_name", nullable = false)
    private String realPositionName;

    @Column(name = "position_catergory", nullable = false)
    private String positionCatergory;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(
            name = "sport_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "sport_id")
    )
    private Sport sport;

    @OneToMany(
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            mappedBy = "position"
    )
    private List<PlayerPosition> playerPositions = new ArrayList<>();

    public Position(Long id, String positionName, String realPositionName, String positionCatergory, LocalDateTime createdAt) {
        this.id = id;
        this.positionName = positionName;
        this.realPositionName = realPositionName;
        this.positionCatergory = positionCatergory;
        this.createdAt = createdAt;
    }
}
