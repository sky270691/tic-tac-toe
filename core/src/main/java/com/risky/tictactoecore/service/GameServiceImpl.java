package com.risky.tictactoecore.service;

import com.risky.tictactoecore.domain.*;
import com.risky.tictactoecore.exception.*;
import com.risky.tictactoecore.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Random;

@RequiredArgsConstructor
@Slf4j
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    @Override
    public GameData loadGame(LoadGameData loadGameData) throws DataNotFoundException {
        if(loadGameData.boardSize() < 3) {
            throw new GameRuleException("board size should greater than 2");
        }

        if(loadGameData.id() != null) {
            return gameRepository.loadGameData(loadGameData.id()).orElseThrow(() -> new RuntimeException("invalid data"));
        }

        log.debug("load new game data");
        return gameRepository.save(generateNewGame(loadGameData));
    }

    @Override
    public GameData onlinePlayer2Join(Player2JoinData joinData) {
        Long gameId = joinData.gameId();
        GameData gameData = loadGameById(gameId);


        if(gameData.player2() != null) {
            throw new GameRuleException("this session already filled with 2 players");
        }

        int randomTurn = randomTurn();
        String lastTurn;
        if(randomTurn == 1) {
            lastTurn = "player1";
        } else  {
            lastTurn = "player2";
        }

        return gameRepository.save(
                new GameData(gameData.gameId(),
                        gameData.gameMode(),
                        GameStatus.ON_GOING,
                        gameData.player1(),
                        "player2",
                        lastTurn,
                        gameData.boardSize(),
                        gameData.playerPos(),
                        gameData.winner())
        );
    }

    @Override
    public GameData submitMove(NewMoveData moveData) throws GameRuleException{
        log.debug("submit move with data: {}", moveData);

        GameData gameData = loadGameById(moveData.gameId());

        if(gameData.gameStatus().equals(GameStatus.DONE) || gameData.gameStatus().equals(GameStatus.NEW)) {
            throw new GameRuleException("game already finished, or not started");
        }

        Game game = Game.loadGame(gameData);

        GameData submittedMoveGameData = game.submitMove(moveData.player(), moveData.posMove());

        return gameRepository.save(submittedMoveGameData);
    }

    private int randomTurn() {
        return new Random().nextInt(1,3);
    }

    private GameData loadGameById(Long id) {
        return gameRepository.loadGameData(id)
                .orElseThrow(() -> new DataNotFoundException("game with id: " + id + " not found"));
    }

    private GameData generateNewGame(LoadGameData loadGameData) {
        if(loadGameData.gameMode().equals(GameMode.SINGLE_DEVICE)) {
            return new GameData(null,
                    loadGameData.gameMode(),
                    GameStatus.ON_GOING,
                    loadGameData.player1(),
                    loadGameData.player2(),
                    null,
                    loadGameData.boardSize(),
                    new HashMap<>(),
                    null);
        }

        return new GameData(null,
                loadGameData.gameMode(),
                GameStatus.NEW,
                loadGameData.player1(),
                null,
                null,
                loadGameData.boardSize(),
                new HashMap<>(),
                null);
    }

}
