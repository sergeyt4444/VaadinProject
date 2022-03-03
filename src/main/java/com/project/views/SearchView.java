package com.project.views;

import com.project.controller.MainControllerInterface;
import com.project.views.components.HeaderPanel;
import com.project.views.components.LatestCoursesPanel;
import com.project.views.components.NavPanel;
import com.project.views.components.SearchPanel;
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

@Route("vaadin_project/search")
@Secured("ROLE_USER")
public class SearchView extends VerticalLayout implements BeforeEnterObserver {

    private HeaderPanel headerPanel;
    private NavPanel navPanel;
    private SearchPanel searchPanel;
    private HorizontalLayout horizontalLayout;
    private MainControllerInterface controllerInterface;
    private FlexLayout footerLayout;

    public SearchView(MainControllerInterface controllerInterface) {

        this.controllerInterface = controllerInterface;
        headerPanel = new HeaderPanel();

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

        searchPanel = new SearchPanel(controllerInterface, beforeEnterEvent);
        navPanel = new NavPanel(controllerInterface);
        horizontalLayout.removeAll();
        horizontalLayout.add(navPanel, searchPanel);
        this.removeAll();
        add(headerPanel, new Hr(), horizontalLayout, footerLayout);

    }
}
