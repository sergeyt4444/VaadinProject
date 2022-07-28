package com.project.views.components;

import com.project.controller.AdminControllerInterface;
import com.project.controller.UserControllerInterface;
import com.project.entity.AttrEnum;
import com.project.entity.Obj;
import com.project.tools.ObjectConverter;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.QueryParameters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersPanel extends VerticalLayout {

    private H2 usersHeader;
    private Grid<Map<Integer, String>> usersGrid;
    private PageNavigationComponent pageNavigationComponent;
    public static final int PAGE_SIZE = 10;

    public UsersPanel(UserControllerInterface controllerInterface, AdminControllerInterface adminControllerInterface,
                      BeforeEnterEvent beforeEnterEvent) {

        usersHeader = new H2("Users");

        Location location = beforeEnterEvent.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();

        int currentPage = 1;
        int pagesCount = (adminControllerInterface.getUsersCount() - 1)/LatestCoursesPanel.PAGE_SIZE + 1;

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

        List<Obj> users = adminControllerInterface.getUsers(currentPage, PAGE_SIZE).getBody();
        List<Map<Integer, String>> mappedUsers = ObjectConverter.convertListOfObjects(users);

        usersGrid = new Grid<>();
        usersGrid.setClassName("user-courses-grid");
        usersGrid.setItems(mappedUsers);

        usersGrid.addColumn(map -> ObjectConverter.getIdFromMappedObj(map)).setHeader("Id")
                .setAutoWidth(true).setFlexGrow(0);
        usersGrid.addColumn(map -> map.get(AttrEnum.USER_NAME.getValue())).setHeader("Username")
                .setResizable(true).setWidth("250px");
        usersGrid.addComponentColumn(map ->{
            Button goToUser = new Button("Courses list");
            goToUser.addClickListener(click -> {
                getUI().ifPresent(ui -> {
                    ui.navigate("vaadin_project/usercourses/" + map.get(AttrEnum.USER_NAME.getValue()));
                });
            });
            return goToUser;
        }).setHeader("Go to user's courses list").setTextAlign(ColumnTextAlign.CENTER)
                .setResizable(true).setWidth("350px");
        usersGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        usersGrid.setHeightByRows(true);

        pageNavigationComponent = new PageNavigationComponent(currentPage, pagesCount, beforeEnterEvent);

        this.setAlignItems(Alignment.CENTER);
        this.setJustifyContentMode(JustifyContentMode.START);
        this.addClassName("users-panel");
        this.add(usersHeader, usersGrid, pageNavigationComponent);


    }

}
