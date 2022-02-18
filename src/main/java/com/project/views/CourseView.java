package com.project.views;

import com.project.controller.MainControllerInterface;
import com.project.entity.Obj;
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
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

import java.util.List;
import java.util.Map;

@Route("vaadin_project/course")
@Secured("ROLE_USER")
public class CourseView extends VerticalLayout {

    private HeaderPanel headerPanel;
    private NavPanel navPanel;
    private CoursePanel coursePanel;
    private HorizontalLayout horizontalLayout;

    public CourseView(MainControllerInterface controllerInterface) {
        this.setHeight("100%");

        UI.getCurrent().getSession().setAttribute("root category id", "0");
        headerPanel = new HeaderPanel();
        navPanel = new NavPanel(controllerInterface);
        Obj course = controllerInterface.getObjectById(27).getBody();
        Map<Integer, String> mappedObj = ObjectConverter.convertObject(course);
        coursePanel = new CoursePanel(mappedObj);
        horizontalLayout = new HorizontalLayout(navPanel, coursePanel);
        horizontalLayout.setHeight("100%");
        horizontalLayout.setMinHeight("700px");
        horizontalLayout.setMinWidth("85%");
        horizontalLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        this.setSizeFull();
        FlexLayout footerLayout = new FlexLayout();
        footerLayout.addClassName("footer-layout");
        Div footer = new Div();
        footer.addClassName("footer");
        footer.setText("Contact info: E-mail: adminmail@mail.ru, phone: 7(999)999-9999");
        footerLayout.setAlignItems(FlexComponent.Alignment.END);
        footerLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        footerLayout.getElement().getStyle().set("order", "999");
        footerLayout.add(footer);

        add(headerPanel, new Hr(), horizontalLayout);
        add(footerLayout);
        expand(footerLayout);
    }

}
