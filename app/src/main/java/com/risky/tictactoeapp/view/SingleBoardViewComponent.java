package com.risky.tictactoeapp.view;

import com.risky.tictactoecore.domain.GameData;
import com.risky.tictactoecore.domain.Pos;
import com.risky.tictactoecore.service.GameService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

//for single device game
public class SingleBoardViewComponent extends VerticalLayout {

    private final ResourceLoader resourceLoader;
    private final GameService gameService;
    private boolean player1Turn;
    private GameData gameData;
    private Paragraph win;

    public SingleBoardViewComponent(ResourceLoader resourceLoader, GameService gameService, GameData gameData) {
        this.gameData = gameData;
        this.resourceLoader = resourceLoader;
        this.gameService = gameService;

        initData();
        initView();
        generateBoard(gameData.boardSize());
        initWinOrLose();
    }

    private void initData() {
        player1Turn = true;
    }

    private void initView() {
        getStyle().setBackgroundColor("#e6e6e6");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.AROUND);
    }
    private void initWinOrLose() {
        Paragraph win = new Paragraph("");
        win.addClassName(LumoUtility.FontSize.XLARGE);
        win.setVisible(false);
        this.win = win;
        addComponentAsFirst(win);
    }

    private void generateBoard(int boardSize) {
        VerticalLayout column = new VerticalLayout();
        column.setPadding(false);
        column.setSizeUndefined();
        column.setSpacing(false);
        for (int i = 1; i <= boardSize; i++) {
            HorizontalLayout row = new HorizontalLayout();
            row.setSpacing(false);
            column.add(row);
            for (int j = 1; j <= boardSize; j++) {
                row.add(generateBox(j + "x," + i + "y"));
            }
            add(column);
        }
    }

    private Div generateBox(String id) {
        Div boxDiv = new Div();
        boxDiv.setWidth("3em");
        boxDiv.setHeight("3em");
        boxDiv.getStyle().setBorder("solid");
        boxDiv.setId(id);

        boxDiv.add(List.copyOf(generateBoxContent(boxDiv)));
        boxDiv.addClickListener(ev -> {
            Component component = boxDiv.getComponentAt(player1Turn ? 0 : 1);
            if(!component.getElement().isVisible()) {
                handleClickedMove(ev, component);
            }
        });
        return boxDiv;
    }

    //handling click square
    private void handleClickedMove(ClickEvent<Div> ev, Component component) {
        Optional<String> pos = ev.getSource().getId();
        GameData gameData = gameService.submitMove(new GameService.NewMoveData(this.gameData.gameId(), player1Turn ? this.gameData.player1() : this.gameData.player2(), extractPos(pos.get())));
        component.setVisible(true);
        String winner = gameData.winner();
        if(winner != null) {
            if(winner.equalsIgnoreCase("player1")) {
                win.setText("PLAYER 1 WIN");
            } else {
                win.setText("PLAYER 2 WIN");
            }
            win.setVisible(true);
        }
        ev.unregisterListener();
        player1Turn = !player1Turn;
    }

    private Pos extractPos(String stringPos) {
        String[] stringPosSplit = stringPos.split(",");
        int x = Integer.parseInt(stringPosSplit[0].split("")[0]);
        int y = Integer.parseInt(stringPosSplit[1].split("")[0]);
        return new Pos(x, y);
    }

    //generate per square
    private List<Image> generateBoxContent(Div parent) {
        Image cross = new Image(new StreamResource("cross", () -> {
            Resource resource = resourceLoader.getResource("classpath:assets/cross.png");
            try {
                return resource.getInputStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }),"cross");

        cross.setMaxHeight(parent.getHeight());
        cross.setWidth(parent.getWidth());
        cross.setVisible(false);

        Image circle = new Image(new StreamResource("circle", () -> {
            Resource resource = resourceLoader.getResource("classpath:assets/circle.png");
            try {
                return resource.getInputStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }),"circle");
        circle.setMaxHeight(parent.getHeight());
        circle.setWidth(parent.getWidth());
        circle.setVisible(false);

        return List.of(circle, cross);
    }

}
