package com.project.views;

import com.project.controller.MainControllerInterface;
import com.project.views.components.HeaderPanel;
import com.project.views.components.MainPanel;
import com.project.views.components.NavPanel;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;


@Route("vaadin_project/user")
@Secured("ROLE_USER")
public class MainView extends VerticalLayout {

    private HeaderPanel headerPanel;
    private NavPanel navPanel;
    private MainPanel mainPanel;
    private HorizontalLayout horizontalLayout;

    public MainView(MainControllerInterface controllerInterface) {

        headerPanel = new HeaderPanel();
        navPanel = new NavPanel();
        mainPanel = new MainPanel(controllerInterface.getMainCategories());
        horizontalLayout = new HorizontalLayout(navPanel, mainPanel);
        horizontalLayout.setMinHeight("700px");
        horizontalLayout.setAlignItems(Alignment.STRETCH);

        add(headerPanel, new Hr(), horizontalLayout);
    }


}
