package com.risky.tictactoeapp.service;

import com.risky.tictactoeapp.view.OnlineMpBoardViewComponent;
import com.risky.tictactoecore.domain.GameData;
import com.risky.tictactoecore.repository.GameRepository;
import com.vaadin.flow.component.UI;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class MultiplayerService {

    private final ConcurrentHashMap<ConcurrentDataUI, UI> uiConcurrentHashMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<ConcurrentDataUI, OnlineMpBoardViewComponent> componentConcurrentHashMap = new ConcurrentHashMap<>();

    private final GameRepository gameRepository;

    @Async
    public CompletableFuture<GameData> checkWaitingRoomReady(GameData gameData) throws InterruptedException {
        int count = 0;
        while (count++ < 101) {
            System.out.println("checking: " + count);
            TimeUnit.SECONDS.sleep(3);
            GameData entity = gameRepository.loadGameData(gameData.gameId())
                    .orElseThrow();
            if (entity.player2() != null) {
                return CompletableFuture.supplyAsync(() -> entity);
            }
        }
        return CompletableFuture.supplyAsync(() -> null);
    }

    public void registerUI(ConcurrentDataUI data, UI ui) {
        uiConcurrentHashMap.put(data, ui);

    }
    public void registerComponent(ConcurrentDataUI data, OnlineMpBoardViewComponent ui) {
        componentConcurrentHashMap.put(data, ui);
    }

    public boolean mpDataExist(ConcurrentDataUI data) {
        return uiConcurrentHashMap.containsKey(data);
    }

    public void triggerUpdate(ConcurrentDataUI data, String divId, boolean win, boolean draw) {
        UI ui = uiConcurrentHashMap.get(data);
        OnlineMpBoardViewComponent comp = componentConcurrentHashMap.get(data);

        ui.access(() -> {
            comp.updateFromAnotherThread(ui, divId, win, draw);
        });
    }

    public record ConcurrentDataUI(Long gameId, String activePlayer) {}

}
