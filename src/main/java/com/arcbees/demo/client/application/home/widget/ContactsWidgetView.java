package com.arcbees.demo.client.application.home.widget;

import java.util.List;

import com.arcbees.demo.client.api.domain.Contact;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class ContactsWidgetView extends ViewWithUiHandlers<ContactsWidgetUiHandlers>
        implements ContactsWidgetPresenter.MyView {
    interface Binder extends UiBinder<HTMLPanel, ContactsWidgetView> {
    }

    @UiField
    Button fetchButton;
    @UiField(provided = true)
    FlexTable contactTable;

    @Inject
    public ContactsWidgetView(Binder binder) {
        initTable();

        initWidget(binder.createAndBindUi(this));
    }

    @Override
    public void setContactTable(List<Contact> contacts) {
        int index = 1;

        for (Contact contact : contacts) {
            contactTable.insertRow(index);
            contactTable.setText(index, 0, contact.getName());
            contactTable.setText(index, 1, contact.getEmail());
            index ++;
        }
    }

    @UiHandler("fetchButton")
    void onFetchContacts(ClickEvent clickEvent) {
        getUiHandlers().fetchContacts();
    }

    private void initTable() {
        contactTable = new FlexTable();
        contactTable.setText(0, 0, "Name");
        contactTable.setText(0, 1, "Email");
    }
}
