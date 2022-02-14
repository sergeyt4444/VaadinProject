package com.project.views;

import com.project.controller.MainControllerInterface;
import com.project.entity.Obj;
import com.project.tools.ObjectConverter;
import com.project.views.components.HeaderPanel;
import com.project.views.components.MainPanel;
import com.project.views.components.NavPanel;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

import java.util.List;
import java.util.Map;


@Route("vaadin_project/main_page")
@Secured("ROLE_USER")
public class MainView extends VerticalLayout {

    private HeaderPanel headerPanel;
    private NavPanel navPanel;
    private MainPanel mainPanel;
    private HorizontalLayout horizontalLayout;

    public MainView(MainControllerInterface controllerInterface) {

        headerPanel = new HeaderPanel();
        navPanel = new NavPanel(controllerInterface);
        List<Obj> objList = controllerInterface.getMainCategories().getBody();
        List<Map<Integer, String>> mappedObjList = ObjectConverter.convertListOfObjects(objList);
        mainPanel = new MainPanel(mappedObjList);
        horizontalLayout = new HorizontalLayout(navPanel, mainPanel);
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
