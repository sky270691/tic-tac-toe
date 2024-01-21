package com.risky.tictactoecore.service;

import com.risky.tictactoecore.domain.GameData;
import com.risky.tictactoecore.domain.LoadGameData;
import com.risky.tictactoecore.domain.Pos;

public interface GameService {

    GameData loadGame(LoadGameData loadGameData);
    GameData onlinePlayer2Join(Player2JoinData joinData);
    GameData submitMove(NewMoveData moveData);

    record NewMoveData(Long gameId, String player, Pos posMove){}
    record Player2JoinData(Long gameId){}
}
