package com.project.views.components;

import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.StreamResource;
import org.keycloak.KeycloakPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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

        StreamResource imageResource = new StreamResource(
                "logo.png",
                () -> getClass().getResourceAsStream("/images/logo.png"));
        logoImage = new Image(imageResource,"Netcracker");
        logoImage.setWidth("180px");
        logoImage.setHeight("100px");

        logo = new Label();
        logo.getElement().setProperty("innerHTML", "<b>Netcracker</b>");
        logo.addClassName("logo");
        logo.getStyle().set("font-size", "44px");
        logo.getStyle().set("color", "mediumblue");

        logoLayout = new HorizontalLayout(logoImage, logo);
        logoLayout.setAlignItems(Alignment.CENTER);
        logoLayout.setMargin(false);

        searchBar = new TextField();
        searchBar.setPlaceholder("Search");
        searchBar.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchBar.setMinWidth("48%");
        searchBar.setMaxWidth("48%");
        searchBar.getStyle().set("font-size", "24px");


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        KeycloakPrincipal principal = ((KeycloakPrincipal) authentication.getPrincipal());
        String username = principal.getKeycloakSecurityContext().getToken().getPreferredUsername();

        userAvatar = new Avatar(username);

        userLabel = new Label(username);
        userLabel.getStyle().set("font-size", "20px");


        profileButton = new Button("Профиль");
        profileButton.addClickListener(click -> {
        });

        currentCoursesButton = new Button("Текущие курсы");
        currentCoursesButton.addClickListener(click -> {
        });

        userButtonLayout = new VerticalLayout(profileButton, currentCoursesButton);
        userButtonLayout.setAlignItems(Alignment.STRETCH);

        userPanelLayout = new HorizontalLayout(userAvatar, userLabel, userButtonLayout);
        userPanelLayout.setAlignItems(Alignment.CENTER);
        userPanelLayout.setMinWidth("15%");

        add(logoLayout, searchBar, userPanelLayout);

        this.setAlignItems(Alignment.CENTER);
        this.setJustifyContentMode(JustifyContentMode.BETWEEN);
        this.setMinWidth("100%");
    }

}
