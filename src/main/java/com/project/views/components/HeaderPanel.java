package com.project.views.components;

import com.project.controller.MainControllerInterface;
import com.project.entity.AttrEnum;
import com.project.entity.Obj;
import com.project.tools.AttributeTool;
import com.project.tools.ObjectConverter;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.Registration;
import org.keycloak.KeycloakPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

@CssImport("./styles/styles.css")
public class HeaderPanel extends HorizontalLayout {

    private Image logoImage;
    private Label logo;
    private TextField searchBar;
    private Button searchButton;
    private Avatar userAvatar;
    private Label userLabel;

    private HorizontalLayout logoLayout;
    private HorizontalLayout userPanelLayout;

    public HeaderPanel(MainControllerInterface controllerInterface) {
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
        final Registration[] registration = new Registration[1];
        searchBar.addFocusListener(textFieldFocusEvent -> {
            registration[0] = Shortcuts.addShortcutListener(searchBar, () ->
            {
                getUI().ifPresent(ui -> {
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("query", searchBar.getValue());
                    ui.navigate("vaadin_project/search", QueryParameters.simple(parameters));
                });
            }, Key.ENTER);
        });
        searchBar.addBlurListener(event -> {
            registration[0].remove();
        });

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        KeycloakPrincipal principal = ((KeycloakPrincipal) authentication.getPrincipal());
        String username = principal.getKeycloakSecurityContext().getToken().getPreferredUsername();

        userAvatar = new Avatar(username);
        userAvatar.setClassName("user-avatar");

        userLabel = new Label(username);
        userLabel.setClassName("user-label");

        MenuBar menuBar = new MenuBar();
        menuBar.setClassName("menu-bar");

        ComponentEventListener<ClickEvent<MenuItem>> logoutListener = click -> {
            getUI().get().getPage().setLocation("http://localhost:8180/auth/realms/myrealm/protocol/openid-connect/logout?redirect_uri=" +
                    "http://localhost:8081/vaadin_project/main_page");
        };

        ComponentEventListener<ClickEvent<MenuItem>> profileListener = click -> {
            getUI().get().getPage().setLocation("http://localhost:8081/vaadin_project/profile");
        };

        MenuItem menuItem = menuBar.addItem(userLabel);
        SubMenu subMenu = menuItem.getSubMenu();
        subMenu.addItem("Profile", profileListener);
        subMenu.addItem("Logout", logoutListener);

        userPanelLayout = new HorizontalLayout(userAvatar, menuBar);
        userPanelLayout.setAlignItems(Alignment.CENTER);
        userPanelLayout.setClassName("user-panel-layout");

        add(logoLayout, searchBar, userPanelLayout);

        this.setAlignItems(Alignment.CENTER);
        this.setJustifyContentMode(JustifyContentMode.BETWEEN);

        Obj user = controllerInterface.getUser(username).getBody();
        Map<Integer, String> mappedUser = ObjectConverter.convertObject(user);
        String userCoursesString = "";
        String notifiedCoursesString = "";
        if (mappedUser.get(AttrEnum.USER_COURSES.getValue()) != null) {
            userCoursesString = mappedUser.get(AttrEnum.USER_COURSES.getValue());
        }
        if (mappedUser.get(AttrEnum.COURSES_NOTIFIED.getValue()) != null) {
            notifiedCoursesString = mappedUser.get(AttrEnum.COURSES_NOTIFIED.getValue());
        }
        List<String> userCourses = Arrays.asList(userCoursesString.split(";"));
        List<String> notifiedCourses = Arrays.asList(notifiedCoursesString.split(";"));
        for (String course: userCourses) {
            if (!notifiedCourses.contains(course) && (course.matches("\\d+"))) {
                Obj courseObj = controllerInterface.getObjectById(Integer.parseInt(course)).getBody();
                Map<Integer, String> mappedCourse = ObjectConverter.convertObject(courseObj);
                if (Integer.parseInt(mappedCourse.get(AttrEnum.CURRENT_PARTICIPANTS.getValue())) >=
                        Integer.parseInt(mappedCourse.get(AttrEnum.PARTICIPANTS_REQUIRED.getValue()))) {
                    Notification notification = new Notification("Your course has enough participants now");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    notification.setPosition(Notification.Position.TOP_END);
                    notification.open();
                    Map<String, String> mappedUserCourses = AttributeTool.convertObjAttr("courses notified",
                            notifiedCoursesString +
                                    ObjectConverter.getIdFromMappedObj(mappedCourse) + ";", ObjectConverter.getIdFromMappedObj(mappedUser));
                    controllerInterface.addUserCourse(mappedUserCourses);
                }
            }
        }

    }

}
