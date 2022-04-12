package com.project.views;

import com.project.controller.AdminControllerInterface;
import com.project.controller.UserControllerInterface;
import com.project.entity.Obj;
import com.project.tools.ObjectConverter;
import com.project.views.components.HeaderPanel;
import com.project.views.components.CategoriesDiv;
import com.project.views.components.NavPanel;
import com.project.views.components.PageNavigationComponent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import org.springframework.security.access.annotation.Secured;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Route("vaadin_project/main_page")
@Secured("ROLE_USER")
public class MainView extends VerticalLayout implements BeforeEnterObserver {

    private HeaderPanel headerPanel;
    private NavPanel navPanel;
    private CategoriesDiv mainPanel;
    private HorizontalLayout horizontalLayout;
    private VerticalLayout verticalLayout;
    private PageNavigationComponent pageNavigationComponent;
    private UserControllerInterface controllerInterface;
    private AdminControllerInterface adminControllerInterface;
    private FlexLayout footerLayout;
    public static final int PAGE_SIZE = 8;

    public MainView(UserControllerInterface controllerInterface, AdminControllerInterface adminControllerInterface) {
        this.controllerInterface = controllerInterface;
        this.adminControllerInterface = adminControllerInterface;

        UI.getCurrent().getSession().setAttribute("root category id", "0");
        headerPanel = new HeaderPanel(controllerInterface);
        horizontalLayout = new HorizontalLayout();
        horizontalLayout.setHeight("100%");
        horizontalLayout.setMinHeight("700px");
        horizontalLayout.setWidthFull();
        horizontalLayout.setAlignItems(Alignment.STRETCH);

        this.setSizeFull();
        footerLayout = new FlexLayout();
        footerLayout.addClassName("footer-layout");
        Div footer = new Div();
        footer.addClassName("footer");
        footer.setText("Contact info: E-mail: adminmail@mail.ru, phone: 7(999)999-9999");
        footerLayout.setAlignItems(Alignment.END);
        footerLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        footerLayout.getElement().getStyle().set("order", "999");
        footerLayout.add(footer);

        add(footerLayout);
        expand(footerLayout);
    }


    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Location location = event.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();
        Map<String, List<String>> queryParamList = queryParameters.getParameters();

        int currentPage = 1;
        int pagesCount = (controllerInterface.getMainCategoriesCount().getBody() - 1)/PAGE_SIZE + 1;

        if (queryParameters.getParameters().containsKey("page")) {
            currentPage = Integer.parseInt(queryParameters.getParameters().get("page").get(0));
            if (currentPage <= 0) {
                currentPage = 1;
                Map<String, String[]> parameters = new HashMap<>();
                parameters.put("page", new String[] {"1"});
                UI.getCurrent().navigate(location.getPath(), QueryParameters.full(parameters));

            }
            if (currentPage > pagesCount) {
                currentPage = pagesCount;
                Map<String, String[]> parameters = new HashMap<>();
                parameters.put("page", new String[] {Integer.toString(pagesCount)});
                UI.getCurrent().navigate(location.getPath(), QueryParameters.full(parameters));
            }
        }

        navPanel = new NavPanel(controllerInterface, adminControllerInterface);
        List<Obj> objList = controllerInterface.getMainCategories(currentPage, PAGE_SIZE).getBody();
        List<Map<Integer, String>> mappedObjList = ObjectConverter.convertListOfObjects(objList);
        mainPanel = new CategoriesDiv(mappedObjList);

        pageNavigationComponent = new PageNavigationComponent(currentPage,pagesCount,event);
        if (pagesCount < 2) {
            pageNavigationComponent.setVisible(false);
        }

        verticalLayout = new VerticalLayout(mainPanel, pageNavigationComponent);
        verticalLayout.setWidth("100%");
        verticalLayout.setHeight("90%");
        verticalLayout.setAlignItems(Alignment.CENTER);

        horizontalLayout.removeAll();
        horizontalLayout.add(navPanel, verticalLayout);
        this.removeAll();
        add(headerPanel, new Hr(), horizontalLayout, footerLayout);
    }
}
