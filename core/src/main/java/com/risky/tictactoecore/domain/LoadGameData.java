package com.risky.tictactoecore.domain;

public record LoadGameData(Long id, GameMode gameMode, String player1, String player2, Integer boardSize) {
    public LoadGameData(Long id) {
        this(id, null, null, null, null);
    }
}
