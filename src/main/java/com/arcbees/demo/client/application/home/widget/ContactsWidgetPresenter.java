package com.arcbees.demo.client.application.home.widget;

import java.util.List;

import com.arcbees.demo.client.api.ContactResource;
import com.arcbees.demo.client.api.domain.Contact;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.client.RestDispatch;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class ContactsWidgetPresenter extends PresenterWidget<ContactsWidgetPresenter.MyView>
        implements ContactsWidgetUiHandlers {
    interface MyView extends View, HasUiHandlers<ContactsWidgetUiHandlers> {
        void setContactTable(List<Contact> contacts);
    }

    private ContactResource contactResource;
    private RestDispatch dispatch;

    @Inject
    public ContactsWidgetPresenter(
            EventBus eventBus,
            MyView view,
            ContactResource contactResource,
            RestDispatch dispatch) {
        super(eventBus, view);

        getView().setUiHandlers( this );

        this.contactResource = contactResource;
        this.dispatch = dispatch;
    }

    @Override
    public void fetchContacts() {
        dispatch.execute(contactResource.getContacts(), new AsyncCallback<List<Contact>>() {
            @Override
            public void onFailure(Throwable throwable) {
                GWT.log(throwable.getMessage());
            }

            @Override
            public void onSuccess(List<Contact> contacts) {
                getView().setContactTable(contacts);
            }
        });
    }
}
