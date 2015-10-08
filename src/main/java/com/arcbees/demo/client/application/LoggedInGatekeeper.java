package com.arcbees.demo.client.application;

import com.arcbees.demo.client.CurrentUser;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.annotations.DefaultGatekeeper;
import com.gwtplatform.mvp.client.proxy.Gatekeeper;

@DefaultGatekeeper
public class LoggedInGatekeeper implements Gatekeeper {
    private CurrentUser currentUser;

    @Inject
    public LoggedInGatekeeper(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public boolean canReveal() {
        return currentUser.isLoggedIn();
    }
}
