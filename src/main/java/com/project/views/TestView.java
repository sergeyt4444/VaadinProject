package com.project.views;

import com.project.controller.DemoSecurityControllerInterface;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;

@Route("")
public class TestView extends VerticalLayout {

    @Autowired
    DemoSecurityControllerInterface controllerInterface;

    public TestView() {
        System.out.println();
        add(new H1("testing " + controllerInterface.getAnonymousInfo()));
    }

}
