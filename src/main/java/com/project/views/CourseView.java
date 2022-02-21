package com.project.views;

import com.project.controller.MainControllerInterface;
import com.project.entity.Obj;
import com.project.entity.ObjectTypeEnum;
import com.project.tools.ObjectConverter;
import com.project.views.components.CategoriesDiv;
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

import java.util.List;
import java.util.Map;

@Route("vaadin_project/course")
@Secured("ROLE_USER")
public class CourseView extends VerticalLayout implements BeforeEnterObserver {

    MainControllerInterface controllerInterface;

    private HeaderPanel headerPanel;
    private NavPanel navPanel;
    private CoursePanel coursePanel;
    private HorizontalLayout horizontalLayout;
    private FlexLayout footerLayout;

    public CourseView(MainControllerInterface controllerInterface) {
        this.controllerInterface = controllerInterface;
        this.setHeight("100%");

        UI.getCurrent().getSession().setAttribute("root category id", "0");
        headerPanel = new HeaderPanel();
        navPanel = new NavPanel(controllerInterface);

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
        int courseId = Integer.parseInt(queryParameters.getParameters().get("id").get(0));
        Obj course = controllerInterface.getObjectById(courseId).getBody();
        if (course == null || course.getObjectType().getObjTypesId() != ObjectTypeEnum.COURSE.getValue()) {
            UI.getCurrent().navigate("404");
        }
        else {

            coursePanel = new CoursePanel(controllerInterface, course);
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
