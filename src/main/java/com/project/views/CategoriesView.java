package com.project.views;

import com.project.controller.AdminControllerInterface;
import com.project.controller.UserControllerInterface;
import com.project.entity.Obj;
import com.project.tools.ObjectConverter;
import com.project.views.components.AllCategoriesComponent;
import com.project.views.components.HeaderPanel;
import com.project.views.components.NavPanel;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

import java.util.List;
import java.util.Map;

@Route("vaadin_project/categories")
@Secured("ROLE_USER")
public class CategoriesView extends VerticalLayout {

    private HeaderPanel headerPanel;
    private NavPanel navPanel;
    private AllCategoriesComponent allCategoriesComponent;
    private HorizontalLayout horizontalLayout;
    private Scroller scroller;

    public CategoriesView(UserControllerInterface controllerInterface, AdminControllerInterface adminControllerInterface) {

        this.setHeight("100%");

        UI.getCurrent().getSession().setAttribute("root category id", "0");
        headerPanel = new HeaderPanel(controllerInterface);
        navPanel = new NavPanel(controllerInterface, adminControllerInterface);
        List<Obj> objList = controllerInterface.getCategories().getBody();
        List<Map<Integer, String>> mappedObjList = ObjectConverter.convertListOfObjects(objList);
        allCategoriesComponent = new AllCategoriesComponent(mappedObjList);
        scroller = new Scroller(allCategoriesComponent);
        scroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
        scroller.addClassName("categories-scroller");
        horizontalLayout = new HorizontalLayout(navPanel, scroller);
        horizontalLayout.setHeight("100%");
        horizontalLayout.setWidthFull();
        horizontalLayout.setMinHeight("700px");
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
