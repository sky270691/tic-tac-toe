package com.risky.tictactoeapp.entity;

import com.risky.tictactoecore.domain.GameMode;
import com.risky.tictactoecore.domain.GameStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Entity
@Table(name = "game_data")
@Getter @Setter
public class GameDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private GameMode gameMode;

    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus;

    @Column(name = "player_1")
    private String player1;

    @Column(name = "player_2")
    private String player2;

    private String lastTurn;

    private Integer boardSize;

    private String winner;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "game_player_pos", joinColumns = @JoinColumn(name = "game_data_id"))
    @Column(name = "player")
    private Map<PosEntity, String> playerPos;

}
