package com.project.views;

import com.project.controller.DemoSecurityControllerInterface;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.keycloak.KeycloakPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Route("/vaadin_project/admin")
@Secured("ROLE_ADMIN")
public class AdminView extends VerticalLayout {

    @Autowired
    DemoSecurityControllerInterface controllerInterface;

    public AdminView() {
        Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(userAuthentication);
        KeycloakPrincipal principal = ((KeycloakPrincipal) userAuthentication.getPrincipal());
        System.out.println(principal.getKeycloakSecurityContext().getToken().toString());
        add(new H1("Admin page"));
    }
}
