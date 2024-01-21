package com.risky.tictactoeapp;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync // to enable async function
@Push // to trigger ui update from another thread so the main class should implement AppShellConfigurator
public class TicTacToeApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(TicTacToeApplication.class, args);
    }

}
