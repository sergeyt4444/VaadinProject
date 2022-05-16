package com.project.views;

import com.project.controller.AdminControllerInterface;
import com.project.controller.ModeratorControllerInterface;
import com.project.controller.UserControllerInterface;
import com.project.entity.Obj;
import com.project.entity.ObjectTypeEnum;
import com.project.views.components.CoursePanel;
import com.project.views.components.HeaderPanel;
import com.project.views.components.NavPanel;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Route("vaadin_project/course")
@Secured("ROLE_USER")
public class CourseView extends VerticalLayout implements BeforeEnterObserver {

    UserControllerInterface controllerInterface;
    ModeratorControllerInterface moderatorControllerInterface;
    AdminControllerInterface adminControllerInterface;

    private HeaderPanel headerPanel;
    private NavPanel navPanel;
    private CoursePanel coursePanel;
    private HorizontalLayout horizontalLayout;
    private FlexLayout footerLayout;

    public CourseView(UserControllerInterface controllerInterface,
                      ModeratorControllerInterface moderatorControllerInterface,
                      AdminControllerInterface adminControllerInterface) {
        this.controllerInterface = controllerInterface;
        this.moderatorControllerInterface = moderatorControllerInterface;
        this.adminControllerInterface = adminControllerInterface;
        this.setHeight("100%");

        UI.getCurrent().getSession().setAttribute("root category id", "0");
        headerPanel = new HeaderPanel(controllerInterface);

        this.setSizeFull();
        footerLayout = new FlexLayout();
        footerLayout.addClassName("footer-layout");
        Div footer = new Div();
        footer.addClassName("footer");
        footer.setText("Contact info: E-mail: adminmail@mail.ru, phone: 7(999)999-9999");
        footerLayout.setAlignItems(FlexComponent.Alignment.END);
        footerLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        footerLayout.getElement().getStyle().set("order", "999");
        footerLayout.add(footer);

    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        Location location = beforeEnterEvent.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();
        if (!queryParameters.getParameters().containsKey("id") ||
                !queryParameters.getParameters().get("id").get(0).matches("\\d+")) {
            UI.getCurrent().navigate("400");
            UI.getCurrent().getPage().reload();
        }
        int courseId = Integer.parseInt(queryParameters.getParameters().get("id").get(0));
        Obj course = controllerInterface.getObjectById(courseId).getBody();
        if (course == null || course.getObjectType().getObjTypesId() != ObjectTypeEnum.COURSE.getValue()) {
            UI.getCurrent().navigate("404");
            UI.getCurrent().getPage().reload();
        }
        else {
            coursePanel = new CoursePanel(controllerInterface, moderatorControllerInterface, course);
            navPanel = new NavPanel(controllerInterface, adminControllerInterface);
            Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();
            if (userAuthentication != null && userAuthentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MODERATOR"))) {
                navPanel.addAttributeManagementButton(controllerInterface, adminControllerInterface);
            }
            if (userAuthentication != null && userAuthentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                navPanel.addCourseDeletionButton(controllerInterface, adminControllerInterface);
            }
            horizontalLayout = new HorizontalLayout(navPanel, coursePanel);
            horizontalLayout.setHeight("100%");
            horizontalLayout.setMinHeight("700px");
            horizontalLayout.setMinWidth("85%");
            horizontalLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
            add(headerPanel, new Hr(), horizontalLayout);
            add(footerLayout);
            expand(footerLayout);
        }
    }
}
