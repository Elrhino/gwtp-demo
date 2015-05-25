package com.arcbees.demo.client.application;

import java.util.logging.Logger;

import javax.inject.Inject;

import com.arcbees.demo.client.place.NameTokens;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

public class ApplicationPresenter extends Presenter<ApplicationPresenter.MyView, ApplicationPresenter.MyProxy>
        implements ApplicationUiHandlers {

    @ProxyStandard
    @NameToken(NameTokens.home)
    interface MyProxy extends ProxyPlace<ApplicationPresenter> {
    }

    interface MyView extends View, HasUiHandlers<ApplicationUiHandlers> {
    }

    private final PlaceManager placeManager;
    private final Logger logger;

    @Inject
    ApplicationPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy,
            PlaceManager placeManager,
            Logger logger) {
        super(eventBus, view, proxy, RevealType.Root);

        this.placeManager = placeManager;
        this.logger = logger;

        getView().setUiHandlers(this);
    }

    @Override
    public void sendName(String name) {
        logger.info(name + " got passed from the view to the presenter!");
    }
}
