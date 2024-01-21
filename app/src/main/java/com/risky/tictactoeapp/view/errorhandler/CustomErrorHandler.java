package com.risky.tictactoeapp.view.errorhandler;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.ErrorHandler;


public class CustomErrorHandler implements ErrorHandler{

    @Override
    public void error(ErrorEvent event) {
        if(UI.getCurrent() != null) {
            UI.getCurrent().access(() -> {
                Notification.show(event.getThrowable().getMessage()).addThemeVariants(NotificationVariant.LUMO_ERROR);
            });
        }
    }
}
