package com.risky.tictactoecore.domain;

import java.util.Map;

public record GameData(
        Long gameId,
        GameMode gameMode,
        GameStatus gameStatus,
        String player1,
        String player2,
        String lastTurn,
        Integer boardSize,
        Map<Pos, String> playerPos,
        String winner) {

}
