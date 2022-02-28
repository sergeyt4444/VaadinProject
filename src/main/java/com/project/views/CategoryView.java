package com.project.views;

import com.project.controller.MainControllerInterface;
import com.project.entity.AttrEnum;
import com.project.entity.Obj;
import com.project.tools.AttributeTool;
import com.project.tools.ObjectConverter;
import com.project.views.components.*;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.security.access.annotation.Secured;

import java.util.*;

@Route("vaadin_project")
@Secured("ROLE_USER")
public class CategoryView extends VerticalLayout implements HasUrlParameter<String>, BeforeEnterObserver {

    public String categoryName;

    private HeaderPanel headerPanel;
    private NavPanel navPanel;
    private CategoryPanel categoryPanel;
    private HorizontalLayout horizontalLayout;
    private MainControllerInterface controllerInterface;
    private FlexLayout footerLayout;
    public static final int PAGE_SIZE = 8;

    public CategoryView(MainControllerInterface controllerInterface) {

        this.controllerInterface = controllerInterface;
        headerPanel = new HeaderPanel();

        horizontalLayout = new HorizontalLayout();
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
    public void setParameter (BeforeEvent event, String parameter){
        categoryName = parameter;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Location location = event.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();
        Map<String, List<String>> queryParamList = queryParameters.getParameters();


        Obj rootObj = controllerInterface.getCategoryByName(categoryName).getBody();
        Map<Integer, String> mappedRootObj = ObjectConverter.convertObject(rootObj);

        List<Obj> subcategoriesObj = controllerInterface.getSubCategories(ObjectConverter.getIdFromMappedObj(mappedRootObj)).getBody();
        List<Map<Integer, String>> mappedSubcategories = ObjectConverter.convertListOfObjects(subcategoriesObj);

        List<Obj> coursesObj;
        int currentPage = 1;
        int pagesCount;
        if (!isFiltered(queryParamList)) {
            pagesCount = (controllerInterface.getCoursesCount(ObjectConverter.getIdFromMappedObj(mappedRootObj)).getBody() - 1)
                    / LatestCoursesPanel.PAGE_SIZE + 1;
        }
        else {
            pagesCount = ((controllerInterface.getFilteredCoursesCount(ObjectConverter.getIdFromMappedObj(mappedRootObj),
                    queryParamList.get("difficulty"), queryParamList.get("language"), queryParamList.get("format")).getBody() - 1)
                    / PAGE_SIZE) + 1;
        }

        if (queryParameters.getParameters().containsKey("page")) {
            currentPage = Integer.parseInt(queryParameters.getParameters().get("page").get(0));
            if (currentPage <= 0) {
                currentPage = 1;
                Map<String, String[]> parameters = new HashMap<>();
                parameters.put("page", new String[] {"1"});
                if (isFiltered(queryParamList)) {
                    parameters.put("difficulty", queryParamList.get("difficulty").toArray(new String[0]));
                    parameters.put("language", queryParamList.get("language").toArray(new String[0]));
                    parameters.put("format", queryParamList.get("format").toArray(new String[0]));
                }
                UI.getCurrent().navigate(location.getPath(), QueryParameters.full(parameters));

            }
            if (currentPage > pagesCount) {
                currentPage = pagesCount;
                Map<String, String[]> parameters = new HashMap<>();
                parameters.put("page", new String[] {Integer.toString(pagesCount)});
                if (isFiltered(queryParamList)) {
                    parameters.put("difficulty", queryParamList.get("difficulty").toArray(new String[0]));
                    parameters.put("language", queryParamList.get("language").toArray(new String[0]));
                    parameters.put("format", queryParamList.get("format").toArray(new String[0]));
                }
                UI.getCurrent().navigate(location.getPath(), QueryParameters.full(parameters));
            }
        }

        if (isFiltered(queryParamList)) {
            if (!queryParamList.containsKey("difficulty")) {
                queryParamList.put("difficulty", AttributeTool.getDifficulties());
            }
            if (!queryParamList.containsKey("language")) {
                queryParamList.put("language", AttributeTool.getLanguages());
            }
            if (!queryParamList.containsKey("format")) {
                queryParamList.put("format", AttributeTool.getFormats());
            }
            coursesObj = controllerInterface.getFilteredCourses(ObjectConverter.getIdFromMappedObj(mappedRootObj),
                    queryParamList.get("difficulty"), queryParamList.get("language"), queryParamList.get("format"),
                    currentPage, PAGE_SIZE).getBody();
        }
        else {
            coursesObj = controllerInterface.getCourses(ObjectConverter.getIdFromMappedObj(mappedRootObj),
                    currentPage, PAGE_SIZE).getBody();
        }

        List<Map<Integer, String>> mappedCourses = ObjectConverter.convertListOfObjects(coursesObj);
        UI.getCurrent().getSession().setAttribute("root category id", ObjectConverter.getIdFromMappedObj(mappedRootObj));

        navPanel = new NavPanel(controllerInterface);
        categoryPanel = new CategoryPanel(controllerInterface, mappedRootObj, mappedSubcategories,
                mappedCourses, queryParamList, currentPage, pagesCount, event);

        horizontalLayout.removeAll();
        horizontalLayout.add(navPanel, categoryPanel);
        this.removeAll();
        add(headerPanel, new Hr(), horizontalLayout, footerLayout);
    }

    private boolean isFiltered(Map<String, List<String>> queryParamList) {
        return (queryParamList.containsKey("difficulty") || queryParamList.containsKey("language") || queryParamList.containsKey("format"));
    }

}
