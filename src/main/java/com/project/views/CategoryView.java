package com.project.views;

import com.project.controller.AdminControllerInterface;
import com.project.controller.ModeratorControllerInterface;
import com.project.controller.UserControllerInterface;
import com.project.entity.Obj;
import com.project.entity.ObjectTypeEnum;
import com.project.tools.AttributeTool;
import com.project.tools.MiscTool;
import com.project.tools.ObjectConverter;
import com.project.views.components.*;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

@Route("vaadin_project")
@Secured("ROLE_USER")
public class CategoryView extends VerticalLayout implements HasUrlParameter<String>, BeforeEnterObserver {

    public String categoryName;

    private HeaderPanel headerPanel;
    private NavPanel navPanel;
    private CategoryPanel categoryPanel;
    private HorizontalLayout horizontalLayout;
    private UserControllerInterface controllerInterface;
    private ModeratorControllerInterface moderatorControllerInterface;
    private AdminControllerInterface adminControllerInterface;
    private FlexLayout footerLayout;
    public static final int PAGE_SIZE = 8;

    public CategoryView(UserControllerInterface controllerInterface,
                        ModeratorControllerInterface moderatorControllerInterface,
                        AdminControllerInterface adminControllerInterface) {

        this.controllerInterface = controllerInterface;
        this.adminControllerInterface = adminControllerInterface;
        headerPanel = new HeaderPanel(controllerInterface);

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
        if (rootObj == null || rootObj.getObjectType().getObjTypesId() != ObjectTypeEnum.CATEGORY.getValue()) {
            UI.getCurrent().navigate("404");
            UI.getCurrent().getPage().reload();
        }
        Map<Integer, String> mappedRootObj = ObjectConverter.convertObject(rootObj);

        List<Obj> subcategoriesObj = controllerInterface.getSubCategories(ObjectConverter.getIdFromMappedObj(mappedRootObj)).getBody();
        List<Map<Integer, String>> mappedSubcategories = ObjectConverter.convertListOfObjects(subcategoriesObj);

        List<Obj> coursesObj;
        int currentPage = 1;
        int pagesCount;
        List<String> difficulties;
        List<String> languages;
        List<String> formats;

        if (queryParamList.containsKey("difficulty")) {
            difficulties = queryParamList.get("difficulty");
        }
        else {
            difficulties = new ArrayList<>();
        }

        if (queryParamList.containsKey("language")) {
            languages = queryParamList.get("language");
        }
        else {
            languages = new ArrayList<>();
        }

        if (queryParamList.containsKey("format")) {
            formats = queryParamList.get("format");
        }
        else {
            formats = new ArrayList<>();
        }

        if (!isFiltered(queryParamList)) {
            pagesCount = (controllerInterface.getCoursesCount(ObjectConverter.getIdFromMappedObj(mappedRootObj)).getBody() - 1)
                    / LatestCoursesPanel.PAGE_SIZE + 1;
        }
        else {
            pagesCount = ((controllerInterface.getFilteredCoursesCount(ObjectConverter.getIdFromMappedObj(mappedRootObj),
                    difficulties, languages, formats).getBody() - 1)
                    / PAGE_SIZE) + 1;
        }

        if (queryParameters.getParameters().containsKey("page")) {
            currentPage = Integer.parseInt(queryParameters.getParameters().get("page").get(0));
            if (currentPage <= 0) {
                currentPage = 1;
                Map<String, String[]> parameters = new HashMap<>();
                parameters.put("page", new String[] {"1"});
                if (isFiltered(queryParamList)) {
                    parameters.put("difficulty", difficulties.toArray(new String[0]));
                    parameters.put("language", languages.toArray(new String[0]));
                    parameters.put("format", formats.toArray(new String[0]));
                }
                UI.getCurrent().navigate(location.getPath(), QueryParameters.full(parameters));

            }
            if (currentPage > pagesCount) {
                currentPage = pagesCount;
                Map<String, String[]> parameters = new HashMap<>();
                parameters.put("page", new String[] {Integer.toString(pagesCount)});
                if (isFiltered(queryParamList)) {
                    parameters.put("difficulty", difficulties.toArray(new String[0]));
                    parameters.put("language", languages.toArray(new String[0]));
                    parameters.put("format", formats.toArray(new String[0]));
                }
                UI.getCurrent().navigate(location.getPath(), QueryParameters.full(parameters));
            }
        }

        if (isFiltered(queryParamList)) {
            coursesObj = controllerInterface.getFilteredCourses(ObjectConverter.getIdFromMappedObj(mappedRootObj),
                    difficulties, languages, formats,
                    currentPage, PAGE_SIZE).getBody();
        }
        else {
            coursesObj = controllerInterface.getCourses(ObjectConverter.getIdFromMappedObj(mappedRootObj),
                    currentPage, PAGE_SIZE).getBody();
        }

        List<Map<Integer, String>> mappedCourses = ObjectConverter.convertListOfObjects(coursesObj);
        UI.getCurrent().getSession().setAttribute("root category id", ObjectConverter.getIdFromMappedObj(mappedRootObj));

        navPanel = new NavPanel(controllerInterface, adminControllerInterface);
        Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();
        if (userAuthentication != null && userAuthentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            navPanel.addCategoryDeletionButton(controllerInterface, moderatorControllerInterface, adminControllerInterface);
        }
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
