package com.project.views;

import com.project.controller.AdminControllerInterface;
import com.project.controller.ModeratorControllerInterface;
import com.project.controller.UserControllerInterface;
import com.project.entity.Obj;
import com.project.entity.ObjectTypeEnum;
import com.project.views.components.*;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import org.springframework.security.access.annotation.Secured;

@Route("vaadin_project/usercourses")
@Secured("ROLE_ADMIN")
public class UserCoursesView  extends VerticalLayout implements HasUrlParameter<String>, BeforeEnterObserver {

    public String username;

    private HeaderPanel headerPanel;
    private NavPanel navPanel;
    private UserCoursesPanel userCoursesPanel;
    private HorizontalLayout horizontalLayout;
    private UserControllerInterface controllerInterface;
    private AdminControllerInterface adminControllerInterface;
    private FlexLayout footerLayout;
    public static final int PAGE_SIZE = 10;

    public UserCoursesView(UserControllerInterface controllerInterface, AdminControllerInterface adminControllerInterface) {
        this.controllerInterface = controllerInterface;
        headerPanel = new HeaderPanel(controllerInterface);

        horizontalLayout = new HorizontalLayout();
        horizontalLayout.setMinHeight("700px");
        horizontalLayout.setWidthFull();
        horizontalLayout.setAlignItems(Alignment.STRETCH);

        this.setSizeFull();
        footerLayout = new FlexLayout();
        footerLayout.addClassName("footer-layout");
        Div footer = new Div();
        footer.addClassName("footer");
        footer.setText("Contact info: E-mail: adminmail@mail.ru, phone: 7(999)999-9999");
        footerLayout.setAlignItems(Alignment.END);
        footerLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        footerLayout.getElement().getStyle().set("order", "999");
        footerLayout.add(footer);

        add(footerLayout);
        expand(footerLayout);
    }

    @Override
    public void setParameter (BeforeEvent event, String parameter){
        username = parameter;
        Obj userObj = controllerInterface.getUser(username).getBody();

        if (userObj == null || userObj.getObjectType().getObjTypesId() != ObjectTypeEnum.USER.getValue()) {
            UI.getCurrent().navigate("404");
            UI.getCurrent().getPage().reload();
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {

        headerPanel = new HeaderPanel(controllerInterface);
        navPanel = new NavPanel(controllerInterface, adminControllerInterface);
        userCoursesPanel = new UserCoursesPanel(controllerInterface, username, beforeEnterEvent);
        horizontalLayout.removeAll();
        horizontalLayout.add(navPanel, userCoursesPanel);
        this.removeAll();
        add(headerPanel, new Hr(), horizontalLayout, footerLayout);

    }

}

