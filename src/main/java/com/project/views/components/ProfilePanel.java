package com.project.views.components;

import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.IDToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

public class ProfilePanel extends VerticalLayout {

    private Avatar avatar;
    private TextField usernameField;
    private TextField roleField;
    private TextField givenNameField;
    private TextField familyNameField;
    private TextField emailField;
    private TextField phoneField;
    private HorizontalLayout buttonLayout;
//    private Button lastCoursesButton;
    private Button currentCoursesButton;

    public ProfilePanel() {

        this.setAlignItems(Alignment.CENTER);
        this.setJustifyContentMode(JustifyContentMode.START);
        this.addClassName("profile-panel");

        KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken)  SecurityContextHolder.getContext().getAuthentication();
        KeycloakPrincipal principal = ((KeycloakPrincipal) authentication.getPrincipal());
        String username = principal.getKeycloakSecurityContext().getToken().getPreferredUsername();
        String gname = "", fname = "", email = "", phone = "";

        if (principal instanceof KeycloakPrincipal) {
            KeycloakPrincipal kPrincipal = (KeycloakPrincipal) principal;
            IDToken token = kPrincipal.getKeycloakSecurityContext().getIdToken();
            email = token.getEmail();
            gname = token.getGivenName();
            fname = token.getFamilyName();

            Map<String, Object> customClaims = token.getOtherClaims();

            if (customClaims.containsKey("phone")) {
                phone = String.valueOf(customClaims.get("phone"));
            }
        }

        avatar = new Avatar(username);
        avatar.setClassName("profile-avatar");

        usernameField = new TextField("Username: ");
        usernameField.setValue(username);
        usernameField.setClassName("profile-tf");
        usernameField.setReadOnly(true);

        roleField = new TextField("Access level: ");
        roleField.setValue(getUserRole(authentication));
        roleField.setClassName("profile-tf");
        roleField.setReadOnly(true);

        givenNameField = new TextField("Given name: ");
        givenNameField.setValue(gname);
        givenNameField.setClassName("profile-tf");
        givenNameField.setReadOnly(true);

        familyNameField = new TextField("Family name: ");
        familyNameField.setValue(fname);
        familyNameField.setClassName("profile-tf");
        familyNameField.setReadOnly(true);

        emailField = new TextField("Email: ");
        emailField.setValue(email);
        emailField.setClassName("profile-tf");
        emailField.setReadOnly(true);

        phoneField = new TextField("Phone: ");
        phoneField.setValue(phone);
        phoneField.setClassName("profile-tf");
        phoneField.setReadOnly(true);

        buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("profile-button-layout");

        currentCoursesButton = new Button("Current courses");
        currentCoursesButton.addClassName("profile-button");
        currentCoursesButton.addClickListener(click -> {

        });

        buttonLayout.add( currentCoursesButton);
        this.add(avatar, usernameField, roleField, givenNameField, familyNameField, emailField, phoneField, buttonLayout);

    }

    private String getUserRole(Authentication authentication) {
        if (authentication != null ) {
            if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                return "Administrator";
            }
            else {
                return "User";
            }
        }
        return "";
    }

}
