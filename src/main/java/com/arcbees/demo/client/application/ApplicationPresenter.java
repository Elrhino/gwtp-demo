package com.arcbees.demo.client.application;

import javax.inject.Inject;

import com.arcbees.demo.client.place.NameTokens;
import com.google.gwt.user.client.Window;
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

    @Inject
    ApplicationPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy,
            PlaceManager placeManager) {
        super(eventBus, view, proxy, RevealType.Root);

        this.placeManager = placeManager;

        getView().setUiHandlers(this);
    }

    @Override
    public void sendName(String name) {
        Window.alert(name + " got passed from the view to the presenter!");
    }
}
