package com.arcbees.demo.client.application.home;

import javax.inject.Inject;

import com.arcbees.demo.client.application.ApplicationPresenter;
import com.arcbees.demo.client.application.home.widget.ContactsWidgetPresenter;
import com.arcbees.demo.client.place.NameTokens;
import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.presenter.slots.Slot;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

public class HomePresenter extends Presenter<HomePresenter.MyView, HomePresenter.MyProxy>
        implements HomeUiHandlers {
    @ProxyStandard
    @NameToken(NameTokens.HOME)
    interface MyProxy extends ProxyPlace<HomePresenter> {
    }

    interface MyView extends View, HasUiHandlers<HomeUiHandlers> {
    }

    public static final Slot SLOT_CONTACTS = new Slot();

    private ContactsWidgetPresenter contactsWidgetPresenter;

    @Inject
    HomePresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy,
            ContactsWidgetPresenter contactsWidgetPresenter) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);
        this.contactsWidgetPresenter = contactsWidgetPresenter;

        getView().setUiHandlers(this);

        setInSlot(SLOT_CONTACTS, contactsWidgetPresenter);
    }

    @Override
    public void sendName(String name) {
        GWT.log(name + " got passed from the view to the presenter!");
    }
}
