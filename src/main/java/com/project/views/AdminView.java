package com.project.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

@Route("/vaadin_project/admin")
@Secured("ROLE_ADMIN")
public class AdminView extends VerticalLayout {

    public AdminView() {
        add(new H1("Admin page"));
    }
}
