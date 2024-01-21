package com.risky.tictactoeapp.view;

import com.risky.tictactoeapp.service.MultiplayerService;
import com.risky.tictactoecore.domain.GameData;
import com.risky.tictactoecore.service.GameService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.core.io.ResourceLoader;

public class OnlineWaitingRoom extends VerticalLayout {

    private final MultiplayerService mpService;
    private final GameService gameService;
    private final ResourceLoader resourceLoader;
    private final GameData gameData;
    private UI ui;

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        ui = attachEvent.getUI();
    }

    public OnlineWaitingRoom(MultiplayerService mpService, GameData gameData, GameService gameService, ResourceLoader resourceLoader) throws InterruptedException{
        this.mpService = mpService;
        this.gameData = gameData;
        this.gameService = gameService;
        this.resourceLoader = resourceLoader;
        initComponent();

        TextField invitationCode = new TextField("Invitation code: ");
        invitationCode.setValue(gameData.gameId().toString());
        invitationCode.setHelperText("give this code to your friend to join");
        invitationCode.setReadOnly(true);
        add(invitationCode);
        triggerWaiting();
    }

    private void initComponent() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }

    private void triggerWaiting() throws InterruptedException {
        mpService.checkWaitingRoomReady(gameData)
                .thenAccept(gd -> {
                    ui.access(() -> {
                        removeAll();
                        add(new OnlineMpBoardViewComponent(resourceLoader, gameService, gd, mpService, gd.player1()));
                    });
                });
    }
}
