package com.project.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("400")
public class BadRequestView extends VerticalLayout {

    public BadRequestView() {
        add(new H1("Bad request"));
    }

}
