package com.risky.tictactoeapp.repository.core;

import com.risky.tictactoeapp.entity.GameDataEntity;
import com.risky.tictactoeapp.entity.PosEntity;
import com.risky.tictactoeapp.repository.jpa.GameJpaRepository;
import com.risky.tictactoecore.domain.GameData;
import com.risky.tictactoecore.domain.Pos;
import com.risky.tictactoecore.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class CoreGameRepository implements GameRepository {

    private final GameJpaRepository gameJpaRepository;

    @Override
    public Optional<GameData> loadGameData(Long gameId) {
        return gameJpaRepository.findById(gameId)
                .map(this::convertFromEntity);
    }

    @Override
    public GameData save(GameData gameData) {

        GameDataEntity entity = gameDataToEntity(gameData);
        GameDataEntity savedEntity = gameJpaRepository.save(entity);
        return convertFromEntity(savedEntity);
    }

    private GameDataEntity gameDataToEntity(GameData gameData) {
        GameDataEntity entity = new GameDataEntity();
        entity.setId(gameData.gameId());
        entity.setGameMode(gameData.gameMode());
        entity.setGameStatus(gameData.gameStatus());
        entity.setPlayer1(gameData.player1());
        entity.setPlayer2(gameData.player2());
        entity.setLastTurn(gameData.lastTurn());
        Map<PosEntity, String> playerPosEntity = gameData.playerPos()
                        .entrySet().stream().collect(Collectors.toMap(key -> {
                    PosEntity posEntity = new PosEntity();
                    posEntity.setX(key.getKey().x());
                    posEntity.setY(key.getKey().y());
                    return posEntity;
                }, Map.Entry::getValue));
        entity.setPlayerPos(playerPosEntity);
        entity.setBoardSize(gameData.boardSize());
        entity.setWinner(gameData.winner());
        return entity;
    }

    private GameData convertFromEntity(GameDataEntity entity) {
        Map<Pos, String> posDomainMap = entity.getPlayerPos()
                .entrySet().stream()
                .collect(Collectors.toMap(key -> new Pos(key.getKey().getX(), key.getKey().getY()), Map.Entry::getValue));

        return new GameData(entity.getId(), entity.getGameMode(), entity.getGameStatus(), entity.getPlayer1(), entity.getPlayer2(), entity.getLastTurn(), entity.getBoardSize(), posDomainMap, entity.getWinner());
    }

}
