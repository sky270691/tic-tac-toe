package com.risky.tictactoeapp.view;

import com.risky.tictactoeapp.service.MultiplayerService;
import com.risky.tictactoecore.domain.GameData;
import com.risky.tictactoecore.service.GameService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.Route;
import org.springframework.core.io.ResourceLoader;

@Route("")
public class MainView extends VerticalLayout {

    private final GameService gameService;
    private final ResourceLoader resourceLoader;
    private final MultiplayerService mpService;

    public MainView(GameService gameService, ResourceLoader resourceLoader, MultiplayerService mpService) {
        this.gameService = gameService;
        this.resourceLoader = resourceLoader;
        this.mpService = mpService;
        initView();
        createLayout();
    }
    private void initView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }

    private void createLayout() {
        HorizontalLayout optionLayout = new HorizontalLayout();
        optionLayout.setAlignItems(Alignment.CENTER);
        optionLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        Button button = new Button("generate game ", event -> {
            UI.getCurrent().navigate("gameMode");
        });

        HorizontalLayout joinGameLayout = getJoinGameLayout(this.gameService, this.resourceLoader, this.mpService);


        optionLayout.add(button);
        optionLayout.add("OR");
        optionLayout.add(joinGameLayout);

        add(optionLayout);
    }


    private HorizontalLayout getJoinGameLayout(GameService gameService, ResourceLoader resourceLoader, MultiplayerService mpService) {
        HorizontalLayout joinGameLayout = new HorizontalLayout();
        VerticalLayout textFieldContainer = new VerticalLayout();

        NumberField invCodeField = new NumberField(null, "input your invitation code");
        invCodeField.setWidth("15em");
        textFieldContainer.add(invCodeField);
        joinGameLayout.add(textFieldContainer);


        joinGameLayout.setAlignItems(Alignment.CENTER);
        Button submitInvCode = new Button("join");
        submitInvCode.addClickListener(ev -> {
            if(invCodeField.isEmpty()) {
                Notification.show("please input player name field and inv code field");
                return;
            }
            long gameId = invCodeField.getValue().longValue();
            GameData gameData = gameService.onlinePlayer2Join(new GameService.Player2JoinData(gameId));
            removeAll();
            add(new OnlineMpBoardViewComponent(resourceLoader, gameService, gameData, mpService, gameData.player2()));
        });
        joinGameLayout.add(submitInvCode);
        return joinGameLayout;
    }


}
