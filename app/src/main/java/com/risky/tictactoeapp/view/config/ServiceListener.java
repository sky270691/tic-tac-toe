package com.risky.tictactoeapp.view.config;

import com.risky.tictactoeapp.view.errorhandler.CustomErrorHandler;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;

@Component
public class ServiceListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addSessionInitListener(e -> e.getSession().setErrorHandler(new CustomErrorHandler()));
    }
}
