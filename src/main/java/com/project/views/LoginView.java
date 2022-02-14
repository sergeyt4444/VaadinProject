package com.project.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;


@Route("vaadin_project/login")
public class LoginView extends VerticalLayout {

    public LoginView() {
        add(new H1("login template"));
    }

}
