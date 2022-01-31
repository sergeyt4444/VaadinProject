package com.project.views;

import com.project.controller.DemoSecurityControllerInterface;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

@Route("/vaadin_project/admin")
@Secured("ROLE_ADMIN")
public class AdminView extends VerticalLayout {

//    @Autowired
//    DemoSecurityControllerInterface controllerInterface;

    public AdminView(DemoSecurityControllerInterface controllerInterface) {
        add(new H1("Admin page"));
    }
}
