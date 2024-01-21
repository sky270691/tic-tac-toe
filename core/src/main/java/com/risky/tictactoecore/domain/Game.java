package com.risky.tictactoecore.domain;

import com.risky.tictactoecore.exception.GameRuleException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Getter
@Slf4j
public class Game {

    private Long gameId;
    private String player1;
    private String player2;
    private Integer boardSize;
    private Map<Pos, String> playerPos;
    private GameMode gameMode;
    private GameStatus gameStatus;
    private String winner;
    private String lastTurn;

    private Game(GameData gameData) {
        this.gameId = gameData.gameId();
        this.gameMode = gameData.gameMode();
        this.player1 = gameData.player1();
        this.player2 = gameData.player2();
        this.boardSize = gameData.boardSize();
        this.playerPos = gameData.playerPos();
        this.lastTurn = gameData.lastTurn();
        this.gameStatus = gameData.gameStatus();
        this.winner = gameData.winner();
    }

    public GameData submitMove(String currentPlayer, Pos posMove) throws GameRuleException {
        // validate player submit
        Rules.validateCurrentPlayerSubmitPos(List.of(player1, player2), currentPlayer);

        // validate turn
        Rules.validateTurn(lastTurn, currentPlayer);

        // validate move range
        Rules.validateMoveRange(boardSize, posMove);

        // validate if posMove already filled or not
        Rules.validateOccupiedPos(playerPos, posMove);

        // proceed with the move
        playerPos.put(posMove, currentPlayer);

        // change turn
        lastTurn = currentPlayer;

        System.out.println(currentPlayer + " moved");

        if(Rules.checkDrawCondition(playerPos.keySet(), boardSize)) {
            this.gameStatus = GameStatus.DONE;
        }

        if(Rules.checkWinCondition(playerPos, boardSize, currentPlayer)) {
            this.gameStatus = GameStatus.DONE;
            this.winner = currentPlayer;
        }

        return generateGameData();
    }

    private GameData generateGameData() {
        return new GameData(gameId, gameMode, gameStatus, player1, player2, lastTurn, boardSize, playerPos, winner);
    }

    public static Game loadGame(GameData gameData) {
        return new Game(gameData);
    }

}
