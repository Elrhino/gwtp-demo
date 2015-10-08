# Beginners's Tutorial - Part 2
In this second part tutorial, we're going to create a login page that will redirect the user to the home page. We're also going to add a search widget to the home page that will send requests to an external API.

# Covered features
* Gatekeeper
* PlaceManager
* PresenterWidget
* NestedSlots
* RestDispatch

# Prerequisites
This tutorial is the second part of [Beginner's Tutorial - Part 1](?).

# Application Structure

```
├── application
│   ├── ApplicationModule.java
│   ├── ApplicationPresenter.java
│   ├── ApplicationView.java
│   ├── ApplicationView.ui.xml
│   ├── SuperDevModeUncaughtExceptionHandler.java
│   ├── home
│   │   ├── HomeModule.java
│   │   ├── HomePresenter.java
│   │   ├── HomeUiHandlers.java
│   │   ├── HomeView.java
│   │   ├── HomeView.ui.xml
│   │   └── widget
│   │       ├── SearchWidgetModule.java
│   │       ├── SearchWidgetPresenter.java
│   │       ├── SearchWidgetUiHandlers.java
│   │       ├── SearchWidgetView.java
│   │       └── ContactsWidgetView.ui.xml
│   └── login
│       ├── LoginModule.java
│       ├── LoginPresenter.java
│       ├── LoginUiHandlers.java
│       ├── LoginView.java
│       └── LoginView.ui.xml
├── gin
│   └── ClientModule.java
└── place
    └── NameTokens.java
```

## Gatekeeper - Creating a login page

[Gatekeepers](?) are used to protect a Presenter from unauthorized access. We're going to need one if we want the user to login before he can access the application.

Let's create the `LoggedInGatekeeper`:

```java
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.annotations.DefaultGatekeeper;
import com.gwtplatform.mvp.client.proxy.Gatekeeper;

@DefaultGatekeeper
public class LoggedInGatekeeper implements Gatekeeper {
    @Override
    public boolean canReveal() {
        // User is logged in
    }
}
```

`@DefaultGatekeeper` is going to tell GWTP to use this Gatekeeper on every Presenter having a ProxyPlace. You can also use the `@NoGatekeeper` annotation above a ProxyPlace interface if you don't want the default Gatekeeper to be applied to this specific Presenter. The `canReveal()` method needs a condition that, when met, will tell GWTP to reveal the Presenter. So, in this case, we need an object that will hold the information when the user logs in. Then, we're going to verify if the user is logged in on `canReveal()`.

```java
import com.arcbees.demo.client.CurrentUser;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.annotations.DefaultGatekeeper;
import com.gwtplatform.mvp.client.proxy.Gatekeeper;

@DefaultGatekeeper
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

Now that we have a Gatekeeper, we're going to create the login page.

Let's create `LoginPresenter` under the `login/` package:

```java
public class LoginPresenter extends Presenter<LoginPresenter.MyView, LoginPresenter.MyProxy>
        implements LoginUiHandlers {
    @ProxyStandard
    @NameToken(NameTokens.LOGIN)
    interface MyProxy extends ProxyPlace<LoginPresenter> {
    }

    interface MyView extends View, HasUiHandlers<LoginUiHandlers> {
    }

    // Credentials are stored here for demo purpose only.
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin123";

    @Inject
    LoginPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);

        getView().setUiHandlers(this);
    }

    private boolean validateCredentials(String username, String password) {
        return username.equals(USERNAME) && password.equals(PASSWORD);
    }
}
```

For the purpose of this tutorial, we're storing the user credentials in the Presenter, but we **strongly discourage** you to do so in a production scenario. We've also added a simple validation method for the username and password.

This is the `LoginView.ui.xml`:

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

And the `LoginView`:

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

The `onConfirm()` method will tell the UiHandler to call `confirm()` on the `LoginPresenter` side.

```java
import com.gwtplatform.mvp.client.UiHandlers;

public interface LoginUiHandlers extends UiHandlers {
    void confirm(String username, String password);
}
```

Now `LoginPresenter` must override `confirm()`:

```java
    @Override
    public void confirm(String username, String password) {
        validateCredentials(username, password);

        currentUser.setLoggedIn(true);
    }
```

## PlaceManager - Navigating to another place

## PresenterWidget - Creating a search widget
A [PresenterWidget](?) allow you to reuse UI components throughout an application. It has the same functionality as a GWT Widget but implements the MVP pattern. Let's say we want to add a very basic "Contact" search functionality. If we were to reuse this functionality on multiple presenters, the best way to achieve this would be with a PresenterWidget.

Let's begin by creating a new package under `home/` and call it `widget`.

Here is the `SearchPresenterWidget`:

```java
public class SearchWidgetPresenter extends PresenterWidget<SearchWidgetPresenter.MyView>
        implements SearchWidgetUiHandlers {
    public interface MyView extends View, HasUiHandlers<SearchWidgetUiHandlers> {
    }

    @Inject
    public SearchWidgetPresenter(
            EventBus eventBus,
            MyView view) {
        super(eventBus, view);

        getView().setUiHandlers(this);
    }

    @Override
    public void search(String searchTerm) {
        // Send request to the server
    }
}
```

And this is the `SearchWidgetView`:

```java
public class SearchWidgetView extends ViewWithUiHandlers<SearchWidgetUiHandlers>
        implements SearchWidgetPresenter.MyView {
    interface Binder extends UiBinder<HTMLPanel, SearchWidgetView> {
    }

    @UiField
    TextBox searchField;
    @UiField
    Button searchButton;

    @Inject
    public SearchWidgetView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("searchButton")
    void onSearch(ClickEvent clickEvent) {
        String searchFieldText = searchField.getText();

        if (!Strings.isNullOrEmpty(searchFieldText)) {
            getUiHandlers().search(searchFieldText);
        }
    }
}
```

We only need two fields for this View, a search box and a confirm button.

```xml
<ui:UiBinder xmlns:ui='urn:ui:com.google.gwt.uibinder'
             xmlns:g='urn:import:com.google.gwt.user.client.ui'>
    <g:HTMLPanel>
        <h3>Search</h3>
        <g:TextBox ui:field="searchField"/>
        <g:Button ui:field="searchButton"/>
    </g:HTMLPanel>
</ui:UiBinder>
```

```java
import com.gwtplatform.mvp.client.UiHandlers;

public interface SearchWidgetUiHandlers extends UiHandlers {
    void search(String searchTerm);
}
```

## RestDispatch - Sending requests to a server
