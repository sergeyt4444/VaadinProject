package com.project.views.components;

import com.project.controller.UserControllerInterface;
import com.project.entity.AttrEnum;
import com.project.entity.Obj;
import com.project.tools.ObjectConverter;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.QueryParameters;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.IDToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CssImport("./styles/styles.css")
public class ProfilePanel extends HorizontalLayout {

    private Avatar avatar;
    private TextField usernameField;
    private TextField roleField;
    private TextField givenNameField;
    private TextField familyNameField;
    private TextField emailField;
    private TextField phoneField;
    private VerticalLayout userInfoLayout;
    private H2 currentCoursesHeader;
    private Grid<Map<Integer, String>> coursesGrid;
    private PageNavigationComponent pageNavigationComponent;
    private VerticalLayout coursesInfoLayout;
    public static final int PAGE_SIZE = 10;

    public ProfilePanel(UserControllerInterface controllerInterface, BeforeEnterEvent beforeEnterEvent) {

        this.setAlignItems(Alignment.START);
        this.setJustifyContentMode(JustifyContentMode.CENTER);
        this.addClassName("full-profile-panel");

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

        userInfoLayout = new VerticalLayout();
        userInfoLayout.setAlignItems(Alignment.CENTER);
        userInfoLayout.setJustifyContentMode(JustifyContentMode.START);
        userInfoLayout.addClassName("profile-panel");
        userInfoLayout.add(avatar, usernameField, roleField, givenNameField, familyNameField, emailField, phoneField);

        currentCoursesHeader = new H2("Current courses");


        Location location = beforeEnterEvent.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();

        int currentPage = 1;
        int pagesCount = (controllerInterface.getUserCoursesCount(username).getBody() - 1)/LatestCoursesPanel.PAGE_SIZE + 1;

        if (queryParameters.getParameters().containsKey("page")) {
            currentPage = Integer.parseInt(queryParameters.getParameters().get("page").get(0));
            if (currentPage <= 0) {
                currentPage = 1;
                Map<String, String> parameters = new HashMap<>();
                parameters.put("page", "1");
                UI.getCurrent().navigate(location.getPath(), QueryParameters.simple(parameters));

            }
            if (currentPage > pagesCount) {
                currentPage = pagesCount;
                Map<String, String> parameters = new HashMap<>();
                parameters.put("page", Integer.toString(pagesCount));
                UI.getCurrent().navigate(location.getPath(), QueryParameters.simple(parameters));
            }
        }

        List<Obj> courses = controllerInterface.getUserCourses(username, currentPage,PAGE_SIZE).getBody();
        List<Map<Integer, String>> mappedCourses = ObjectConverter.convertListOfObjects(courses);

        coursesGrid = new Grid<>();
        coursesGrid.setClassName("user-courses-grid");
        coursesGrid.setItems(mappedCourses);

        coursesGrid.addColumn(map -> ObjectConverter.getIdFromMappedObj(map)).setHeader("Id")
                .setAutoWidth(true).setFlexGrow(0);
        coursesGrid.addColumn(map -> map.get(AttrEnum.COURSE_NAME.getValue())).setHeader("Course name")
                .setResizable(true).setWidth("195px");
        coursesGrid.addColumn(map -> map.get(AttrEnum.COURSE_DESCRIPTION.getValue())).setHeader("Course description")
                .setResizable(true).setWidth("270px");
        coursesGrid.addColumn(map -> map.get(AttrEnum.START_DATE.getValue())).setHeader("Starts on")
                .setAutoWidth(true).setFlexGrow(0);
        coursesGrid.addColumn(map -> (map.get(AttrEnum.CURRENT_PARTICIPANTS.getValue()) + "/" +
                map.get(AttrEnum.PARTICIPANTS_REQUIRED.getValue()))).setHeader("Participants")
                .setAutoWidth(true).setFlexGrow(0);

        coursesGrid.addComponentColumn(map ->{
            Icon icon = new Icon("arrow-forward");
            Button goToCourse = new Button(icon);
            goToCourse.setClassName("goto-button");
            goToCourse.addClickListener(click -> {
                getUI().ifPresent(ui -> {
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("id", Integer.toString(ObjectConverter.getIdFromMappedObj(map)));
                    ui.navigate("vaadin_project/course", QueryParameters.simple(parameters));
                });
            });
            return goToCourse;
        }).setHeader("Go to").setAutoWidth(true).setFlexGrow(0);

        coursesGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        coursesGrid.setHeightByRows(true);

        pageNavigationComponent = new PageNavigationComponent(currentPage, pagesCount, beforeEnterEvent);

        coursesInfoLayout = new VerticalLayout();
        coursesInfoLayout.setAlignItems(Alignment.CENTER);
        coursesInfoLayout.setJustifyContentMode(JustifyContentMode.START);
        coursesInfoLayout.addClassName("courses-info-layout");
        coursesInfoLayout.add(currentCoursesHeader, coursesGrid, pageNavigationComponent);

        this.add(userInfoLayout, coursesInfoLayout);

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
