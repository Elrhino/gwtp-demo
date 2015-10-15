package com.arcbees.demo.client.application.home;

import javax.inject.Inject;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class HomeView extends ViewWithUiHandlers<HomeUiHandlers> implements HomePresenter.MyView {
    interface Binder extends UiBinder<Widget, HomeView> {
    }

    @UiField
    TextBox nameField;
    @UiField
    Button sendButton;
    @UiField
    SimplePanel contactPanel;

    @Inject
    HomeView(
            Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));

        bindSlot(HomePresenter.SLOT_CONTACTS, contactPanel);
    }

    @UiHandler("sendButton")
    void onSend(ClickEvent event) {
        getUiHandlers().sendName(nameField.getText());
    }
}