package com.arcbees.demo.client.application.home.widget;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ContactsWidgetModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindSingletonPresenterWidget(ContactsWidgetPresenter.class, ContactsWidgetPresenter.MyView.class,
                ContactsWidgetView.class);
    }
}
