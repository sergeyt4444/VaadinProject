package com.project.views;

import com.project.controller.MainControllerInterface;
import com.project.entity.Obj;
import com.project.tools.ObjectConverter;
import com.project.views.components.HeaderPanel;
import com.project.views.components.NavPanel;
import com.project.views.components.ProfilePanel;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

import java.util.List;
import java.util.Map;

@Route("vaadin_project/profile")
@Secured("ROLE_USER")
public class ProfileView extends VerticalLayout {

    private HeaderPanel headerPanel;
    private NavPanel navPanel;
    private ProfilePanel profilePanel;
    private HorizontalLayout horizontalLayout;

    public ProfileView(MainControllerInterface controllerInterface) {

        headerPanel = new HeaderPanel();
        navPanel = new NavPanel(controllerInterface);
        profilePanel = new ProfilePanel();
        horizontalLayout = new HorizontalLayout(navPanel, profilePanel);
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
