package com.project.views.components;

import com.project.controller.MainControllerInterface;
import com.project.entity.AttrEnum;
import com.project.entity.Obj;
import com.project.tools.ObjectConverter;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.QueryParameters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LatestCoursesPanel extends VerticalLayout {

    private H2 latestCoursesHeader;
    private Grid<Map<Integer, String>> coursesGrid;
    private PageNavigationComponent pageNavigationComponent;
    public static final int PAGE_SIZE = 10;

    public LatestCoursesPanel(MainControllerInterface controllerInterface, BeforeEvent event) {

        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        addClassName("latest-courses-panel");

        Location location = event.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();
        int currentPage = 1;
        int pagesCount = (controllerInterface.getCoursesCount().getBody() - 1)/LatestCoursesPanel.PAGE_SIZE + 1;

        if (queryParameters.getParameters().containsKey("page")) {
            currentPage = Integer.parseInt(queryParameters.getParameters().get("page").get(0));
            if (currentPage <= 0) {
                currentPage = 1;
                Map<String, String> parameters = new HashMap<>();
                parameters.put("page", "1");
                UI.getCurrent().navigate(location.getPath(), QueryParameters.simple(parameters));

            }
            if (currentPage > pagesCount) {
                currentPage = pagesCount;
                Map<String, String> parameters = new HashMap<>();
                parameters.put("page", Integer.toString(pagesCount));
                UI.getCurrent().navigate(location.getPath(), QueryParameters.simple(parameters));
            }
        }

        latestCoursesHeader = new H2("Latest courses");
        latestCoursesHeader.setClassName("latest-courses-header");

        List<Obj> courses = controllerInterface.getLatestCourses(currentPage, PAGE_SIZE).getBody();
        List<Map<Integer, String>> mappedCourses = ObjectConverter.convertListOfObjects(courses);

        coursesGrid = new Grid<>();
        coursesGrid.setClassName("latest-courses-grid");
        coursesGrid.setItems(mappedCourses);

        coursesGrid.addColumn(map -> ObjectConverter.getIdFromMappedObj(map)).setHeader("Id")
                .setAutoWidth(true).setFlexGrow(0);
        coursesGrid.addColumn(map -> map.get(AttrEnum.COURSE_NAME.getValue())).setHeader("Course name")
                .setResizable(true).setWidth("200px");
        coursesGrid.addColumn(map -> map.get(AttrEnum.COURSE_DESCRIPTION.getValue())).setHeader("Course description")
                .setResizable(true).setWidth("275px");
        coursesGrid.addColumn(map -> map.get(AttrEnum.START_DATE.getValue())).setHeader("Starts on")
                .setAutoWidth(true).setFlexGrow(0);
        coursesGrid.addColumn(map -> (map.get(AttrEnum.CURRENT_PARTICIPANTS.getValue()) + "/" +
                        map.get(AttrEnum.PARTICIPANTS_REQUIRED.getValue()))).setHeader("Participants")
                .setAutoWidth(true).setFlexGrow(0);

        coursesGrid.addComponentColumn(map ->{
            Icon icon = new Icon("arrow-forward");
            Button goToCourse = new Button(icon);
            goToCourse.addClickListener(click -> {
                getUI().ifPresent(ui -> {
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("id", Integer.toString(ObjectConverter.getIdFromMappedObj(map)));
                    ui.navigate("vaadin_project/course", QueryParameters.simple(parameters));
                });
            });
            return goToCourse;
        }).setHeader("Go to").setAutoWidth(true).setFlexGrow(0);

        coursesGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        pageNavigationComponent = new PageNavigationComponent(currentPage, pagesCount, event);

        add(latestCoursesHeader, coursesGrid, pageNavigationComponent);

    }

}
