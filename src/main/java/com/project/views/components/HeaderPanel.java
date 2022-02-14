package com.project.views.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import org.keycloak.KeycloakPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@CssImport("./styles/styles.css")
public class HeaderPanel extends HorizontalLayout {

    private Image logoImage;
    private Label logo;
    private TextField searchBar;
    private Avatar userAvatar;
    private Label userLabel;
    private Button profileButton;
    private Button currentCoursesButton;

    private HorizontalLayout logoLayout;
    private HorizontalLayout userPanelLayout;
    private VerticalLayout userButtonLayout;

    public HeaderPanel() {
        this.setClassName("header-panel");

        StreamResource imageResource = new StreamResource(
                "logo.png",
                () -> getClass().getResourceAsStream("/images/logo.png"));
        logoImage = new Image(imageResource,"Netcracker");
        logoImage.setClassName("logo-image");

        logo = new Label();
        logo.getElement().setProperty("innerHTML", "<b>Netcracker</b>");
        logo.addClassName("logo");

        logoLayout = new HorizontalLayout(logoImage, logo);
        logoLayout.setAlignItems(Alignment.CENTER);
        logoLayout.setClassName("logo-layout");

        searchBar = new TextField();
        searchBar.setPlaceholder("Search");
        searchBar.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchBar.setClassName("search-bar");


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        KeycloakPrincipal principal = ((KeycloakPrincipal) authentication.getPrincipal());
        String username = principal.getKeycloakSecurityContext().getToken().getPreferredUsername();

        userAvatar = new Avatar(username);
        userAvatar.setClassName("user-avatar");

        userLabel = new Label(username);
        userLabel.setClassName("user-label");


        profileButton = new Button("Профиль");
        profileButton.addClickListener(click -> {
        });

        currentCoursesButton = new Button("Текущие курсы");
        currentCoursesButton.addClickListener(click -> {
        });

//        userButtonLayout = new VerticalLayout(profileButton, currentCoursesButton);
//        userButtonLayout.setAlignItems(Alignment.STRETCH);

        MenuBar menuBar = new MenuBar();
        menuBar.setClassName("menu-bar");

        ComponentEventListener<ClickEvent<MenuItem>> listener = click -> {
            getUI().get().getPage().setLocation("http://localhost:8081/sso/logout");
        };

        MenuItem menuItem = menuBar.addItem(userLabel);
        SubMenu subMenu = menuItem.getSubMenu();
        subMenu.addItem("Профиль");
//        subMenu.addItem("Текущие курсы");
        subMenu.addItem("Выход", listener);

        userPanelLayout = new HorizontalLayout(userAvatar, menuBar);
        userPanelLayout.setAlignItems(Alignment.CENTER);
        userPanelLayout.setClassName("user-panel-layout");

        add(logoLayout, searchBar, userPanelLayout);

        this.setAlignItems(Alignment.CENTER);
        this.setJustifyContentMode(JustifyContentMode.BETWEEN);
    }

}
