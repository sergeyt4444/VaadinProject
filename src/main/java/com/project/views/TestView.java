package com.project.views;

import com.project.controller.DemoSecurityControllerInterface;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class TestView extends VerticalLayout {

    public TestView(DemoSecurityControllerInterface controllerInterface) {
        System.out.println();
        add(new H1("testing " + controllerInterface.getAnonymousInfo()));
    }

}
