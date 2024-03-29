package com.project.views;

import com.project.controller.AdminControllerInterface;
import com.project.controller.UserControllerInterface;
import com.project.views.components.*;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import org.springframework.security.access.annotation.Secured;

@Route("vaadin_project/latest_courses")
@Secured("ROLE_USER")
public class LatestCoursesView extends VerticalLayout implements BeforeEnterObserver {


    private HeaderPanel headerPanel;
    private NavPanel navPanel;
    private LatestCoursesPanel latestCoursesPanel;
    private HorizontalLayout horizontalLayout;
    private UserControllerInterface controllerInterface;
    private AdminControllerInterface adminControllerInterface;
    private FlexLayout footerLayout;

    public LatestCoursesView(UserControllerInterface controllerInterface, AdminControllerInterface adminControllerInterface) {

        this.controllerInterface = controllerInterface;
        this.adminControllerInterface = adminControllerInterface;
        headerPanel = new HeaderPanel(controllerInterface);

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

        latestCoursesPanel = new LatestCoursesPanel(controllerInterface, beforeEnterEvent);
        navPanel = new NavPanel(controllerInterface, adminControllerInterface);
        horizontalLayout.removeAll();
        horizontalLayout.add(navPanel, latestCoursesPanel);
        this.removeAll();
        add(headerPanel, new Hr(), horizontalLayout, footerLayout);

    }
}
