package com.project.views;

import com.project.controller.DemoSecurityControllerInterface;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

@Route("vaadin_project/user")
@Secured("ROLE_USER")
public class UserView extends VerticalLayout {

//    @Autowired
//    DemoSecurityControllerInterface controllerInterface;

    public UserView(DemoSecurityControllerInterface controllerInterface) {
        add(new H1("User page" + controllerInterface.getUserInfo()));
    }

}
