package com.project.views;

import com.project.controller.AdminControllerInterface;
import com.project.controller.UserControllerInterface;
import com.project.views.components.HeaderPanel;
import com.project.views.components.NavPanel;
import com.project.views.components.ProfilePanel;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

@Route("vaadin_project/profile")
@Secured("ROLE_USER")
public class ProfileView extends VerticalLayout implements BeforeEnterObserver {

    private UserControllerInterface controllerInterface;
    private AdminControllerInterface adminControllerInterface;
    private HeaderPanel headerPanel;
    private NavPanel navPanel;
    private ProfilePanel profilePanel;
    private HorizontalLayout horizontalLayout;
    private FlexLayout footerLayout;

    public ProfileView(UserControllerInterface controllerInterface, AdminControllerInterface adminControllerInterface) {

        this.controllerInterface = controllerInterface;
        this.adminControllerInterface = adminControllerInterface;
        UI.getCurrent().getSession().setAttribute("root category id", "0");

        horizontalLayout = new HorizontalLayout();
        horizontalLayout.setMinHeight("700px");
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
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {

        headerPanel = new HeaderPanel(controllerInterface);
        navPanel = new NavPanel(controllerInterface, adminControllerInterface);
        profilePanel = new ProfilePanel(controllerInterface, beforeEnterEvent);
        horizontalLayout.removeAll();
        horizontalLayout.add(navPanel, profilePanel);
        this.removeAll();
        add(headerPanel, new Hr(), horizontalLayout, footerLayout);

    }
}
