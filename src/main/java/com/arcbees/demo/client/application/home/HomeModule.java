package com.arcbees.demo.client.application.home;

import com.arcbees.demo.client.application.home.widget.ContactsWidgetModule;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class HomeModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new ContactsWidgetModule());

        bindPresenter(HomePresenter.class, HomePresenter.MyView.class, HomeView.class,
                HomePresenter.MyProxy.class);
    }
}
