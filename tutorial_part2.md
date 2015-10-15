# Beginner's Tutorial - Part 2
This second part of the tutorial will show you key features of GWTP. Namely, protecting your application using Gatekeepers, using the PlaceManager to navigate between Places, creating a PresenterWidget and make requests to an external API using RestDispatch.

# Covered features
* [Gatekeeper](#Gatekeeper)
* [PlaceManager](#PlaceManager)
* [PresenterWidget](#PresenterWidget)
* [RestDispatch](#RestDispatch)

# Prerequisites
This tutorial assumes that you read the [first part](?) or that you understand how Views and Presenters interact together.

# Application Structure

```
├── api
│   ├── ApiPaths.java
│   ├── Contact.java
│   ├── ContactResource.java
│   └── RestDispatchModule.java
├── application
│   ├── ApplicationModule.java
│   ├── ApplicationPresenter.java
│   ├── ApplicationView.java
│   ├── ApplicationView.ui.xml
│   ├── home
│   │   ├── HomeModule.java
│   │   ├── HomePresenter.java
│   │   ├── HomeUiHandlers.java
│   │   ├── HomeView.java
│   │   ├── HomeView.ui.xml
│   │   └── widget
│   │       ├── ContactsWidgetModule.java
│   │       ├── ContactsWidgetPresenter.java
│   │       ├── ContactsWidgetUiHandlers.java
│   │       ├── ContactsWidgetView.java
│   │       └── ContactsWidgetView.ui.xml
│   ├── LoggedInGatekeeper.java
│   └── login
│       ├── LoginModule.java
│       ├── LoginPresenter.java
│       ├── LoginUiHandlers.java
│       ├── LoginView.java
│       └── LoginView.ui.xml
├── CurrentUser.java
├── gin
│   └── ClientModule.java
└── place
    └── NameTokens.java

```

## Gatekeeper
[Gatekeepers](?) are used to protect a Presenter from unauthorized access. They are often used on an application requiring a login page or an administration section that should not be visible without permission. When a Gatekeeper is applied to a Presenter, it will prevent the Presenter to be revealed unless the `gatekeeper.canReveal()` method returns `true`.

This is an implementation of a Gatekeeper:

```java
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.annotations.DefaultGatekeeper;
import com.gwtplatform.mvp.client.proxy.Gatekeeper;

@DefaultGatekeeper
public class LoggedInGatekeeper implements Gatekeeper {
    @Override
    public boolean canReveal() {
        // The user must be logged in
    }
}
```

The next snippet shows you the same Gatekeeper, however `canReveal()` has to verify if the current used is logged in.

```java
import com.arcbees.demo.client.CurrentUser;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.proxy.Gatekeeper;

public class LoggedInGatekeeper implements Gatekeeper {
    private CurrentSession currentUser;

    @Inject
    public LoggedInGatekeeper(CurrentSession currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public boolean canReveal() {
        return currentUser.isLoggedIn();
    }
}
```

Once the Gatekeeper is defined, there is multiple ways to apply it on Presenters. Here's 3 annotations that will help you accomplish this task:
1. Using the `@DefaultGatekeeper` annotation above your **Gatekeeper** class declaration will tell GWTP to use this Gatekeeper on every Presenters in the application.
1. The `@NoGatekeeper` annotation above a **Presenter's ProxyPlace** will tell GWTP to skip the default Gatekeeper.
1. When a specific Gatekeeper should be applied to a Presenter, the `@UseGatekeeper(LoggedInGatekeeper.class)` annotation should be used above a **Presenter's ProxyPlace**.

This is the LoginPresenter:

```java
import javax.inject.Inject;

import com.arcbees.demo.client.application.ApplicationPresenter;
import com.arcbees.demo.client.place.NameTokens;
import com.arcbees.demo.client.CurrentUser;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

public class LoginPresenter extends Presenter<LoginPresenter.MyView, LoginPresenter.MyProxy>
        implements LoginUiHandlers {
    @ProxyStandard
    @NameToken(NameTokens.LOGIN)
    @NoGatekeeper
    interface MyProxy extends ProxyPlace<LoginPresenter> {
    }

    interface MyView extends View, HasUiHandlers<LoginUiHandlers> {
    }

    // Credentials are stored here for demo purpose only.
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin123";

    private CurrentUser currentUser;

    @Inject
    LoginPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy,
            CurrentUser currentUser) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);

        this.placeManager = placeManager;
        getView().setUiHandlers(this);
    }

    @Override
    public void confirm(String username, String password) {
        if (validateCredentials(username, password)) {
            currentUser.setLoggedIn(true);

            // TODO: Navigate to the HomePresenter
        }
    }

    private boolean validateCredentials(String username, String password) {
        return username.equals(USERNAME) && password.equals(PASSWORD);
    }
}
```

Notice the `@NoGatekeeper` annotation above the ProxyPlace. The LoginPresenter will need to be revealed when the user enters its credentials. This is why the Gatekeeper is skipped on this Presenter.

**NOTE:** User credentials should never be stored on the client-side.

This is the LoginView.ui.xml:

```xml
<ui:UiBinder xmlns:ui='urn:ui:com.google.gwt.uibinder'
             xmlns:g='urn:import:com.google.gwt.user.client.ui'>
    <g:HTMLPanel>
        <g:TextBox ui:field="username"/><br/>
        <g:PasswordTextBox ui:field="password"/><br/>
        <g:Button ui:field="confirm" text="Login"/>
    </g:HTMLPanel>
</ui:UiBinder>
```

And the LoginView:

```java
import javax.inject.Inject;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class LoginView extends ViewWithUiHandlers<LoginUiHandlers> implements LoginPresenter.MyView {
    interface Binder extends UiBinder<Widget, LoginView> {
    }

    @UiField
    Button confirm;
    @UiField
    TextBox username;
    @UiField
    PasswordTextBox password;

    @Inject
    LoginView(
            Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("confirm")
    void onConfirm(ClickEvent event) {
        getUiHandlers().confirm(username.getText(), password.getText());
    }
}
```

## PlaceManager
The [PlaceManager](?) allow you to navigate from one Place to another by using the `revealPlace()` method. The PlaceManager needs to be injected into the Presenter's constructor and needs a [PlaceRequest](?) object in order to call `revealPlace(placeRequest)`.

From the LoginPresenter, a PlaceRequest can be built using the `PlaceRequest.Builder()`:

```java
@Override
public void confirm(String username, String password) {
    if (validateCredentials(username, password)) {
        currentUser.setLoggedIn(true);

        PlaceRequest placeRequest = new PlaceRequest.Builder()
                .nameToken(NameTokens.HOME)
                .build();
        placeManager.revealPlace(placeRequest);
    }
}
```

The HomePresenter will now be revealed after a user enters the right credentials.

## PresenterWidget
A [PresenterWidget](?) allow you to reuse UI components throughout an application. It has the same functionality as a GWT Widget but with the flexibility of a View-Presenter pair. PresenterWidgets cannot be Places. This means that you cannot navigate to them. They are nested inside a Presenter using the [slot mechanism](?).

This is the ContactsWidgetPresenter under `home/widget/`:

```java
import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class ContactsWidgetPresenter extends PresenterWidget<ContactsWidgetPresenter.MyView>
        implements ContactsWidgetUiHandlers {
    interface MyView extends View, HasUiHandlers<ContactsWidgetUiHandlers> {
    }

    @Inject
    public ContactsWidgetPresenter(
            EventBus eventBus,
            MyView view) {
        super(eventBus, view);

        getView().setUiHandlers(this);
    }
}
```

Notice that there is no `MyProxy` interface extending `ProxyPlace`. Again, this is because a PresenterWidget cannot be a Place. There is no `RevealContent` event in `super()` either because a PresenterWidget is revealed by its parent Presenter.

**NOTE**: The process to associate a View and a UiHandlers to a PresenterWidget does not change from a regular Presenter.

Binding a PresenterWidget is a little bit different than binding a Presenter. Instead of using the `bindPresenter()` method, you can either use `bindPresenterWidget()` or `  bindSingletonPresenterWidget()` if you wish to have only one instance of the PresenterWidget.

```java
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ContactsWidgetModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindSingletonPresenterWidget(ContactsWidgetPresenter.class, ContactsWidgetPresenter.MyView.class,
                ContactsWidgetView.class);
    }
}
```

In order for the parent Presenter to reveal a PresenterWidget, the parent must define a [Slot](?) for the child to be set into. This can be achieve by defining a field `public static final Slot SLOT_CONTENT = new Slot();` and then using the `setInSlot()` method to set the PresenterWidget into its parent's slot.

This is the HomePresenter:

```java
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

    ...
}
```

After the PresenterWidget is set in the Slot, the parent's View needs a container for the PresenterWidget to be displayed into. When the container is defined, it can be binded to the Presenter's Slot using `bindSlot()`.

This is the HomeView.ui.xml:

```xml
<ui:UiBinder xmlns:ui='urn:ui:com.google.gwt.uibinder'
             xmlns:g='urn:import:com.google.gwt.user.client.ui'>
    <g:HTMLPanel>
        <g:TextBox ui:field="nameField"/>
        <g:Button ui:field="sendButton" text="submit"/>
        <g:SimplePanel ui:field="contactPanel"/>
    </g:HTMLPanel>
</ui:UiBinder>
```

This is the HomeView:

```java
@UiField
SimplePanel contactPanel;

@Inject
HomeView(
        Binder uiBinder) {
    initWidget(uiBinder.createAndBindUi(this));

    bindSlot(HomePresenter.SLOT_CONTACTS, contactPanel);
}
```

## RestDispatch
[RestDispatch](?) is a client library used to communicate to a server in a RESTful manner. It can be used independently of GWTP for any GWT project.
