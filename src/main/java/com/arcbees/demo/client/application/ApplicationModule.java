package com.arcbees.demo.client.application;

import com.arcbees.demo.client.application.home.HomeModule;
import com.arcbees.demo.client.application.login.LoginModule;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ApplicationModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new LoginModule());
        install(new HomeModule());

        bindPresenter(ApplicationPresenter.class, ApplicationPresenter.MyView.class, ApplicationView.class,
                ApplicationPresenter.MyProxy.class);
    }
}
