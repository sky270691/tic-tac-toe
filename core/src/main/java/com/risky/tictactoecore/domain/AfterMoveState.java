package com.risky.tictactoecore.domain;

import java.util.Map;

public record AfterMoveState(Long gameId, String lastTurn, Boolean finished, String winner, Map<Pos, String> playerPos) {}
