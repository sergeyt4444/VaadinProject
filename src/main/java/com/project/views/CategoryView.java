package com.project.views;

import com.project.controller.MainControllerInterface;
import com.project.entity.AttrEnum;
import com.project.entity.Obj;
import com.project.tools.ObjectConverter;
import com.project.views.components.CategoryPanel;
import com.project.views.components.HeaderPanel;
import com.project.views.components.NavPanel;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.security.access.annotation.Secured;

import java.util.List;
import java.util.Map;

@Route("vaadin_project")
@Secured("ROLE_USER")
public class CategoryView extends VerticalLayout implements HasUrlParameter<String> {

    public String categoryName;

    private HeaderPanel headerPanel;
    private NavPanel navPanel;
    private CategoryPanel categoryPanel;
    private HorizontalLayout horizontalLayout;
    private MainControllerInterface controllerInterface;

    public CategoryView(MainControllerInterface controllerInterface) {

        this.controllerInterface = controllerInterface;
        headerPanel = new HeaderPanel();

        horizontalLayout = new HorizontalLayout();
        horizontalLayout.setMinHeight("700px");
        horizontalLayout.setAlignItems(Alignment.STRETCH);

        this.setSizeFull();
        FlexLayout footerLayout = new FlexLayout();
        footerLayout.addClassName("footer-layout");
        Div footer = new Div();
        footer.addClassName("footer");
        footer.setText("Contact info: E-mail: adminmail@mail.ru, phone: 7(999)999-9999");
        footerLayout.setAlignItems(Alignment.END);
        footerLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        footerLayout.getElement().getStyle().set("order", "999");
        footerLayout.add(footer);

        add(headerPanel, new Hr(), horizontalLayout);
        add(footerLayout);
        expand(footerLayout);
    }


    @Override
    public void setParameter (BeforeEvent event, String parameter){
        categoryName = parameter;

        Obj rootObj = controllerInterface.getCategoryByName(categoryName).getBody();
        Map<Integer, String> mappedRootObj = ObjectConverter.convertObject(rootObj);

        List<Obj> subcategoriesObj = controllerInterface.getSubCategories(ObjectConverter.getIdFromMappedObj(mappedRootObj)).getBody();
        List<Map<Integer, String>> mappedSubcategories = ObjectConverter.convertListOfObjects(subcategoriesObj);

        List<Obj> coursesObj = controllerInterface.getCourses(ObjectConverter.getIdFromMappedObj(mappedRootObj)).getBody();
        List<Map<Integer, String>> mappedCourses = ObjectConverter.convertListOfObjects(coursesObj);

        UI.getCurrent().getSession().setAttribute("root category id", ObjectConverter.getIdFromMappedObj(mappedRootObj));

        navPanel = new NavPanel(controllerInterface);
        categoryPanel = new CategoryPanel(controllerInterface, mappedRootObj, mappedSubcategories, mappedCourses);

        horizontalLayout.removeAll();
        horizontalLayout.add(navPanel, categoryPanel);
    }


}
