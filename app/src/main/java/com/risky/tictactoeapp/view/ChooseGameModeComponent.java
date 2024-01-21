package com.risky.tictactoeapp.view;

import com.risky.tictactoeapp.service.MultiplayerService;
import com.risky.tictactoecore.domain.GameData;
import com.risky.tictactoecore.domain.GameMode;
import com.risky.tictactoecore.domain.LoadGameData;
import com.risky.tictactoecore.service.GameService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.Route;
import org.springframework.core.io.ResourceLoader;

@Route("gameMode")
public class ChooseGameModeComponent extends VerticalLayout {

    private final ResourceLoader resourceLoader;
    private final GameService gameService;
    private final MultiplayerService multiplayerService;
    private NumberField boardSizeField;

    public ChooseGameModeComponent(ResourceLoader resourceLoader, GameService gameService, MultiplayerService multiplayerService) {
        this.resourceLoader = resourceLoader;
        this.gameService = gameService;
        this.multiplayerService = multiplayerService;
        initComponent();
        displayChoice();
        boardSizeInput();
    }

    private void initComponent() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }

    private void displayChoice() {
        HorizontalLayout layout = new HorizontalLayout();
        Button singleDeviceMode = new Button("Single Device Mode");
        singleDeviceMode.setHeight("10em");
        singleDeviceMode.setWidth("15em");
        singleDeviceMode.addClickListener(ev -> {
            handleSingleDeviceModeClick();
        });

        Button multipleDeviceMode = new Button("Online Mode");
        multipleDeviceMode.setHeight("10em");
        multipleDeviceMode.setWidth("15em");
        multipleDeviceMode.addClickListener(ev -> {
            handleMultiplayerOnlineClick();
        });

        layout.add(singleDeviceMode, multipleDeviceMode);

        add(layout);
    }

    private void handleMultiplayerOnlineClick() {
        if(boardSizeField.isInvalid() || boardSizeField.isEmpty()) {
            throw new IllegalArgumentException("please input correct board size");
        }
        removeAll();
        GameData gameData = gameService.loadGame(new LoadGameData(null, GameMode.MULTIPLAYER_ONLINE, "player1", null, boardSizeField.getValue().intValue()));
        try {
            add(new OnlineWaitingRoom(multiplayerService, gameData, gameService, resourceLoader));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleSingleDeviceModeClick() {
        if(boardSizeField.isInvalid() || boardSizeField.isEmpty()) {
            throw new IllegalArgumentException("please input correct board size");
        }
        removeAll();
        GameData gameData = gameService.loadGame(new LoadGameData(null, GameMode.SINGLE_DEVICE, "player1", "player2", boardSizeField.getValue().intValue()));
        add(new SingleBoardViewComponent(resourceLoader, gameService, gameData));
    }

    private void boardSizeInput() {
        NumberField textField = new NumberField();
        textField.setMax(12);
        textField.setMin(3);
        textField.setPlaceholder("board size");
        textField.setLabel("choose between 3 - 12");
        this.boardSizeField = textField;
        addComponentAsFirst(textField);
    }



}
