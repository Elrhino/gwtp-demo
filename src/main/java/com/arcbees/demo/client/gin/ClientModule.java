package com.arcbees.demo.client.gin;

import com.arcbees.demo.client.application.ApplicationModule;
import com.arcbees.demo.client.place.NameTokens;
import com.arcbees.demo.client.CurrentUser;
import com.gwtplatform.dispatch.rest.client.RestApplicationPath;
import com.gwtplatform.dispatch.rest.client.gin.RestDispatchAsyncModule;
import com.gwtplatform.mvp.client.annotations.DefaultPlace;
import com.gwtplatform.mvp.client.annotations.ErrorPlace;
import com.gwtplatform.mvp.client.annotations.UnauthorizedPlace;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;

/**
 * See more on setting up the PlaceManager on <a
 * href="// See more on: https://github.com/ArcBees/GWTP/wiki/PlaceManager">DefaultModule's > DefaultPlaceManager</a>
 */
public class ClientModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new DefaultModule());
        install(new ApplicationModule());

        // DefaultPlaceManager Places
        bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.LOGIN);
        bindConstant().annotatedWith(ErrorPlace.class).to(NameTokens.LOGIN);
        bindConstant().annotatedWith(UnauthorizedPlace.class).to(NameTokens.LOGIN);

        RestDispatchAsyncModule.Builder dispatchBuilder = new RestDispatchAsyncModule.Builder();
        install(dispatchBuilder.build());

        bindConstant().annotatedWith(RestApplicationPath.class).to("http://toaster-launcher-api.appspot.com");

        bind(CurrentUser.class).asEagerSingleton();
    }
}
