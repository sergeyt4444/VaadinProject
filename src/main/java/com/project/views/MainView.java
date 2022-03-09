package com.project.views;

import com.project.controller.AdminControllerInterface;
import com.project.controller.UserControllerInterface;
import com.project.entity.Obj;
import com.project.tools.ObjectConverter;
import com.project.views.components.HeaderPanel;
import com.project.views.components.CategoriesDiv;
import com.project.views.components.NavPanel;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

import java.util.List;
import java.util.Map;


@Route("vaadin_project/main_page")
@Secured("ROLE_USER")
public class MainView extends VerticalLayout {

    private HeaderPanel headerPanel;
    private NavPanel navPanel;
    private CategoriesDiv mainPanel;
    private HorizontalLayout horizontalLayout;

    public MainView(UserControllerInterface controllerInterface, AdminControllerInterface adminControllerInterface) {

        this.setHeight("100%");

        UI.getCurrent().getSession().setAttribute("root category id", "0");
        headerPanel = new HeaderPanel(controllerInterface);
        navPanel = new NavPanel(controllerInterface, adminControllerInterface);
        List<Obj> objList = controllerInterface.getMainCategories().getBody();
        List<Map<Integer, String>> mappedObjList = ObjectConverter.convertListOfObjects(objList);
        mainPanel = new CategoriesDiv(mappedObjList);
        horizontalLayout = new HorizontalLayout(navPanel, mainPanel);
        horizontalLayout.setHeight("100%");
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


}
