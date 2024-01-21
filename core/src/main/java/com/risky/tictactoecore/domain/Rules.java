package com.risky.tictactoecore.domain;

import com.risky.tictactoecore.exception.GameRuleException;

import java.util.*;
import java.util.stream.Collectors;

class Rules {

    static void validateCurrentPlayerSubmitPos(List<String> listedPlayer, String submitPlayer) {

        if(listedPlayer.stream().noneMatch(lp -> lp.equalsIgnoreCase(submitPlayer))) {
            throw new GameRuleException("You are not a valid listed player in this game");
        }

    }

    static void validateTurn(String previousTurn, String submitPlayer) {
        if(previousTurn != null && previousTurn.equalsIgnoreCase(submitPlayer)) {
            throw new GameRuleException("this is not your turn");
        }
    }

    static void validateMoveRange(Integer boardSize, Pos move) throws GameRuleException {
        boolean invalidMoveX = move.x() > boardSize || move.x() < 1;
        boolean invalidMoveY = move.y() > boardSize || move.y() < 1;
        if(invalidMoveX || invalidMoveY) {
            throw new GameRuleException("Move range can't be greater than board size or less than 1");
        }
    }

    static void validateOccupiedPos(Map<Pos, String> playerPos, Pos newPos) throws GameRuleException {
        boolean posOccupied = playerPos.entrySet().stream()
                .anyMatch(posEntry -> posEntry.getKey().equals(newPos));

        if(posOccupied) {
            throw new GameRuleException("pos already occupied, consider another move");
        }
    }

    static boolean checkWinCondition(Map<Pos, String> playerPosMap, Integer boardSize, String currentPlayer) {
        Map<String, List<Pos>> playerPosList = playerPosMap.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.mapping(Map.Entry::getKey, Collectors.toList())));

        List<Pos> currentPlayerPosList = playerPosList.get(currentPlayer);

        return checkPosDataWin(currentPlayerPosList, boardSize);
    }

    static boolean checkDrawCondition(Set<Pos> posList, Integer boardSize) {
        return posList.size() == (boardSize * boardSize);
    }

    private static boolean checkPosDataWin(List<Pos> posList, Integer boardSize) {

        boolean diagonalWin = checkDiagonal(posList, boardSize);
        boolean verticalWin = checkHorizontalOrVertical(posList, boardSize, true);
        boolean horizontalWin = checkHorizontalOrVertical(posList, boardSize, false);

        return diagonalWin || verticalWin || horizontalWin;
    }

    private static boolean checkDiagonal(List<Pos> posList, Integer boardSize) {
        List<Pos> firstDiagonal = new ArrayList<>();
        List<Pos> secondDiagonal = new ArrayList<>();

        for (int i = 1; i <= boardSize; i++) {
            final int currentIteration = i;
            posList.forEach(p -> {
                // check top left to down right diagonal
                if(p.x() == currentIteration && p.y() == currentIteration) {
                    firstDiagonal.add(p);
                }

                // check top right to down left diagonal
                int yPair = Math.abs(p.x() - (boardSize + 1));
                if(p.equals(new Pos(currentIteration, yPair))) {
                    secondDiagonal.add(p);
                }
            });
        }

        return (firstDiagonal.size() == boardSize) || (secondDiagonal.size() == boardSize);
    }

    private static boolean checkHorizontalOrVertical(List<Pos> posList, Integer boardSize, boolean vertical) {
        Map<Integer, List<Pos>> linePos = new HashMap<>();

        for (int i = 1; i <= boardSize; i++) {
            final int currentIteration = i;
            posList.forEach(p -> {
                int line;
                if(vertical) {
                    line = p.x();
                } else {
                    line = p.y();
                }
                if (line == currentIteration) {
                    linePos.compute(currentIteration, (k, v) -> {
                        if (v == null) {
                            v = new ArrayList<>();
                        }
                        v.add(p);
                        return v;
                    });
                }
            });

        }

        for (Integer key : linePos.keySet()) {
            if(linePos.get(key).size() == boardSize) {
                return true;
            }
        }

        return false;
    }

}