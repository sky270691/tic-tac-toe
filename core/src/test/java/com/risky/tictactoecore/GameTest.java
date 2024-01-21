package com.risky.tictactoecore;

import com.risky.tictactoecore.domain.*;
import com.risky.tictactoecore.exception.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class GameTest {

    @Test
    @DisplayName("submitMove() without anyone win and without exception occured")
    void submitMoveNoWinNoExceptionTest() {
        GameData gameData = new GameData(1L, GameMode.SINGLE_DEVICE, GameStatus.NEW,"player1", "player2", null, 3, new HashMap<>(), null);
        Game game = Game.loadGame(gameData);

        game.submitMove("player1", new Pos(2,3));
        Assertions.assertEquals(1, game.getPlayerPos().size());
        Assertions.assertEquals("player1", game.getLastTurn());

    }

    @Test
    @DisplayName("submitMove() win by player2 all direction without exception occured")
    void submitMoveWinAllDirectionByPlayer2Test() {
        // win vertical
        Map<Pos, String> mockPlayerPos = new HashMap<>();
        mockPlayerPos.put(new Pos(1,1), "player2");
        mockPlayerPos.put(new Pos(1,2), "player2");
        mockPlayerPos.put(new Pos(2,1), "player1");
        mockPlayerPos.put(new Pos(2,2), "player1");
        GameData gameData = new GameData(1L, GameMode.SINGLE_DEVICE, GameStatus.ON_GOING, "player1", "player2", "player1", 3, mockPlayerPos, null);
        Game game = Game.loadGame(gameData);

        GameData gameDataProduced = game.submitMove("player2", new Pos(1, 3));
        Assertions.assertEquals(5, game.getPlayerPos().size());
        Assertions.assertEquals("player2", game.getLastTurn());
        Assertions.assertEquals(GameStatus.DONE, gameDataProduced.gameStatus());
        Assertions.assertEquals("player2", gameDataProduced.winner());

        // win horizontal
        Map<Pos, String> mockPlayerPos2 = new HashMap<>();
        mockPlayerPos2.put(new Pos(1,1), "player2");
        mockPlayerPos2.put(new Pos(2,1), "player2");
        mockPlayerPos2.put(new Pos(2,2), "player1");
        mockPlayerPos2.put(new Pos(3,2), "player1");
        GameData gameData2 = new GameData(1L, GameMode.SINGLE_DEVICE, GameStatus.ON_GOING, "player1", "player2", "player1", 3, mockPlayerPos2, null);
        Game game2 = Game.loadGame(gameData2);

        GameData gameDataProduced2 = game2.submitMove("player2", new Pos(3, 1));
        Assertions.assertEquals(5, game2.getPlayerPos().size());
        Assertions.assertEquals("player2", game2.getLastTurn());
        Assertions.assertEquals(GameStatus.DONE, gameDataProduced2.gameStatus());
        Assertions.assertEquals("player2", gameDataProduced2.winner());

        // win diagonal
        Map<Pos, String> mockPlayerPos3 = new HashMap<>();
        mockPlayerPos3.put(new Pos(1,3), "player2");
        mockPlayerPos3.put(new Pos(2,2), "player2");
        mockPlayerPos3.put(new Pos(1,2), "player1");
        mockPlayerPos3.put(new Pos(3,2), "player1");
        GameData gameData3 = new GameData(1L, GameMode.SINGLE_DEVICE, GameStatus.ON_GOING, "player1", "player2", "player1", 3, mockPlayerPos3, null);
        Game game3 = Game.loadGame(gameData3);

        GameData gameDataProduced3 = game3.submitMove("player2", new Pos(3, 1));
        Assertions.assertEquals(5, game3.getPlayerPos().size());
        Assertions.assertEquals("player2", game3.getLastTurn());
        Assertions.assertEquals(GameStatus.DONE, gameDataProduced3.gameStatus());
        Assertions.assertEquals("player2", gameDataProduced3.winner());

        Map<Pos, String> mockPlayerPos4 = new HashMap<>();
        mockPlayerPos4.put(new Pos(4,1), "player2");
        mockPlayerPos4.put(new Pos(3,2), "player2");
        mockPlayerPos4.put(new Pos(2,3), "player2");
        mockPlayerPos4.put(new Pos(1,1), "player1");
        mockPlayerPos4.put(new Pos(1,2), "player1");
        mockPlayerPos4.put(new Pos(4,2), "player1");
        GameData gameData4 = new GameData(1L, GameMode.SINGLE_DEVICE, GameStatus.ON_GOING, "player1", "player2", "player1", 4, mockPlayerPos4, null);
        Game game4 = Game.loadGame(gameData4);

        GameData gameDataProduced4 = game4.submitMove("player2", new Pos(1, 4));
        Assertions.assertEquals(7, game4.getPlayerPos().size());
        Assertions.assertEquals("player2", game4.getLastTurn());
        Assertions.assertEquals(GameStatus.DONE, gameDataProduced4.gameStatus());
        Assertions.assertEquals("player2", gameDataProduced4.winner());
    }

    @Test
    @DisplayName("submitMove() throw exception")
    void submitMoveWithExceptionTest() {
        GameData gameData = new GameData(1L, GameMode.SINGLE_DEVICE, GameStatus.NEW, "player1", "player2", null, 3, new HashMap<>(), null);
        Game game = Game.loadGame(gameData);

        Assertions.assertThrows(GameRuleException.class, () -> game.submitMove("player3", new Pos(2,3)));


        GameData gameData2 = new GameData(1L, GameMode.SINGLE_DEVICE, GameStatus.NEW, "player1", "player2", "player1", 3, new HashMap<>(), null);
        Game game2 = Game.loadGame(gameData2);

        Assertions.assertThrows(GameRuleException.class, () -> game2.submitMove("player1", new Pos(2,3)));

        GameData gameData3 = new GameData(1L, GameMode.SINGLE_DEVICE, GameStatus.NEW, "player1", "player2", null, 3, new HashMap<>(), null);
        Game game3 = Game.loadGame(gameData3);

        Assertions.assertThrows(GameRuleException.class, () -> game3.submitMove("player1", new Pos(4,3)));
        Assertions.assertThrows(GameRuleException.class, () -> game3.submitMove("player1", new Pos(0,3)));


        HashMap<Pos, String> playerPos = new HashMap<>();
        playerPos.put(new Pos(2,2), "player1");
        playerPos.put(new Pos(1,1), "player2");
        GameData gameData4 = new GameData(1L, GameMode.SINGLE_DEVICE, GameStatus.ON_GOING, "player1", "player2", null, 3, playerPos, null);
        Game game4 = Game.loadGame(gameData4);

        Assertions.assertThrows(GameRuleException.class, () -> game4.submitMove("player1", new Pos(2,2)));

    }
}