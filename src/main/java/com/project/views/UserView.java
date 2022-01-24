package com.project.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

@Route("vaadin_project/user")
@Secured("ROLE_USER")
public class UserView extends VerticalLayout {

    public UserView() {
        add(new H1("User page"));
    }

}
