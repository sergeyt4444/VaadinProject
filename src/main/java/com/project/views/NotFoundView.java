package com.project.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("404")
public class NotFoundView extends VerticalLayout {

    public NotFoundView() {
        add(new H1("Page not found"));
    }

}
