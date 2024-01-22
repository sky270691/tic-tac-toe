package com.risky.tictactoeapp.view;

import com.risky.tictactoeapp.service.MultiplayerService;
import com.risky.tictactoecore.domain.GameData;
import com.risky.tictactoecore.domain.GameStatus;
import com.risky.tictactoecore.domain.Pos;
import com.risky.tictactoecore.service.GameService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
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

public class OnlineMpBoardViewComponent extends VerticalLayout {

    private final ResourceLoader resourceLoader;
    private final GameService gameService;
    private final MultiplayerService multiplayerService;
    private final GameData initGameData;
    private final String activePlayer;
    private VerticalLayout verticalLayout;
    private Paragraph lose;
    private Paragraph win;
    private Paragraph draw;
    private Paragraph turnPrompt;

    public OnlineMpBoardViewComponent(ResourceLoader resourceLoader, GameService gameService, GameData initGameData, MultiplayerService multiplayerService, String activePlayer) {
        this.initGameData = initGameData;
        this.multiplayerService = multiplayerService;
        this.resourceLoader = resourceLoader;
        this.gameService = gameService;
        this.activePlayer = activePlayer;

        initTurnPrompt();
        initView();
        initBoard(initGameData.boardSize());
        initWinOrLose();
    }


    @Override
    protected void onAttach(AttachEvent attachEvent) {
        UI ui = attachEvent.getUI();
        if(!multiplayerService.mpDataExist(new MultiplayerService.ConcurrentDataUI(initGameData.gameId(), activePlayer))) {
            multiplayerService.registerUI(new MultiplayerService.ConcurrentDataUI(initGameData.gameId(), activePlayer), ui);
            multiplayerService.registerComponent(new MultiplayerService.ConcurrentDataUI(initGameData.gameId(), activePlayer), this);
        }
    }

    private void initTurnPrompt() {
        Paragraph paragraph = new Paragraph();
        if(initGameData.lastTurn().equalsIgnoreCase("player1")) {
            if(activePlayer.equalsIgnoreCase("player1")) {
                paragraph = new Paragraph("Opponent's Turn");
            } else {
                paragraph = new Paragraph("Your Turn");
            }
        }

        add(paragraph);
        this.turnPrompt = paragraph;
    }

    private void initWinOrLose() {
        Paragraph win = new Paragraph("YOU WIN");
        Paragraph lose = new Paragraph("YOU LOSE");
        Paragraph draw = new Paragraph("DRAW");
        win.addClassName(LumoUtility.FontSize.XLARGE);
        win.setVisible(false);
        lose.addClassName(LumoUtility.FontSize.XLARGE);
        lose.setVisible(false);
        draw.addClassName(LumoUtility.FontSize.XLARGE);
        draw.setVisible(false);
        addComponentAsFirst(win);
        addComponentAsFirst(lose);
        addComponentAsFirst(draw);
        this.win = win;
        this.lose = lose;
        this.draw = draw;
    }

    private void initView() {
        getStyle().setBackgroundColor("#e6e6e6");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.AROUND);
    }

    private void initBoard(int boardSize) {
        VerticalLayout column = new VerticalLayout();
        column.setPadding(false);
        column.setSizeUndefined();
        column.setSpacing(false);
        for (int i = 1; i <= boardSize; i++) {
            HorizontalLayout row = new HorizontalLayout();
            row.setSpacing(false);
            column.add(row);
            for (int j = 1; j <= boardSize; j++) {
                row.add(initBox(j + "x," + i + "y"));
            }
            add(column);
        }
        this.verticalLayout = column;
    }

    private Div initBox(String id) {
        Div boxDiv = new Div();
        boxDiv.setWidth("3em");
        boxDiv.setHeight("3em");
        boxDiv.getStyle().setBorder("solid");
        boxDiv.setId(id);

        boxDiv.add(List.copyOf(generateBoxContent(boxDiv)));
        boxDiv.addClickListener(ev -> {
            Component component = boxDiv.getComponentAt(activePlayer.equalsIgnoreCase("player1") ? 0 : 1);
            if(!component.getElement().isVisible()) {
                GameData gameData = handleClickedMove(ev, component);
                boolean drawCondition = gameData.gameStatus().equals(GameStatus.DONE) && gameData.winner() == null;
                multiplayerService.triggerUpdate(new MultiplayerService.ConcurrentDataUI(initGameData.gameId(), activePlayer.equalsIgnoreCase("player1") ? "player2" : "player1"), id, gameData.winner() != null, drawCondition);
                if (gameData.winner() != null) {
                    win.setVisible(true);
                }
                if(drawCondition) {
                    draw.setVisible(true);
                }
            }
        });
        return boxDiv;
    }

    private GameData handleClickedMove(ClickEvent<Div> ev, Component component) {
        Optional<String> pos = ev.getSource().getId();

        GameData gameData = gameService.submitMove(new GameService.NewMoveData(initGameData.gameId(), activePlayer, extractPos(pos.get())));
        if(turnPrompt.isVisible()) {
            turnPrompt.setVisible(false);
        }
        component.setVisible(true);
        ev.unregisterListener();
        return gameData;
    }

    private Pos extractPos(String stringPos) {
        String[] stringPosSplit = stringPos.split(",");
        int x = Integer.parseInt(stringPosSplit[0].split("")[0]);
        int y = Integer.parseInt(stringPosSplit[1].split("")[0]);
        return new Pos(x, y);
    }

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

    public void updateFromAnotherThread(UI ui, String id, boolean opponentWin, boolean drawStatus) {
        ui.access(() -> {
            System.out.println(activePlayer + " updated from opponent thread");
            Optional<Div> any = verticalLayout.getChildren()
                    .flatMap(Component::getChildren)
                    .map(d -> (Div) d)
                    .filter(d -> d.getId().get().equalsIgnoreCase(id))
                    .peek(d -> d.getId().ifPresent(System.out::println))
                    .findAny();
            Component com = any.get().getComponentAt(activePlayer.equalsIgnoreCase("player2") ? 0 : 1);
            com.setVisible(true);
            turnPrompt.setVisible(false);
            if(opponentWin) {
                lose.setVisible(true);;
            }
            if(drawStatus) {
                draw.setVisible(true);
            }
        });
    }

}
