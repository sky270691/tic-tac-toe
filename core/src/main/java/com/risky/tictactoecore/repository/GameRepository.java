package com.risky.tictactoecore.repository;

import com.risky.tictactoecore.domain.GameData;

import java.util.Optional;

public interface GameRepository {

    Optional<GameData> loadGameData(Long gameId);
    GameData save(GameData gameData);


}
