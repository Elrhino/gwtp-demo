package com.arcbees.demo.client.application;

import javax.inject.Inject;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.UiHandlers;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.proxy.Proxy;

public class ApplicationPresenter extends Presenter<ApplicationPresenter.MyView, ApplicationPresenter.MyProxy>
        implements UiHandlers {

    @ProxyStandard
    interface MyProxy extends Proxy<ApplicationPresenter> {
    }

    interface MyView extends View {
    }

    // Slot that holds the HomePresenter
    public static final NestedSlot SLOT_MAIN_CONTENT = new NestedSlot();

    @Inject
    ApplicationPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy) {
        super(eventBus, view, proxy, RevealType.Root);
    }
}
