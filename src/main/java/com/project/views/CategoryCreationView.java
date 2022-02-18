package com.project.views;

import com.project.controller.MainControllerInterface;
import com.project.views.components.CategoryCreationPanel;
import com.project.views.components.HeaderPanel;
import com.project.views.components.NavPanel;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;


@Route("vaadin_project/course_creation")
@Secured("ROLE_ADMIN")
public class CategoryCreationView extends VerticalLayout {

    private HeaderPanel headerPanel;
    private NavPanel navPanel;
    private CategoryCreationPanel courseCreationPanel;
    private HorizontalLayout horizontalLayout;

    public CategoryCreationView(MainControllerInterface controllerInterface) {

        headerPanel = new HeaderPanel();
        navPanel = new NavPanel(controllerInterface);
        courseCreationPanel = new CategoryCreationPanel(controllerInterface, null);
        horizontalLayout = new HorizontalLayout(navPanel, courseCreationPanel);
        horizontalLayout.setMinHeight("700px");
        horizontalLayout.setAlignItems(Alignment.STRETCH);

        this.setSizeFull();
        FlexLayout footerLayout = new FlexLayout();
        footerLayout.addClassName("footer-layout");
        Div footer = new Div();
        footer.addClassName("footer");
        footer.setText("Contact info: E-mail: adminmail@mail.ru, phone: 7(999)999-9999");
        footerLayout.setAlignItems(Alignment.END);
        footerLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        footerLayout.getElement().getStyle().set("order", "999");
        footerLayout.add(footer);

        add(headerPanel, new Hr(), horizontalLayout);
        add(footerLayout);
        expand(footerLayout);
    }


}
