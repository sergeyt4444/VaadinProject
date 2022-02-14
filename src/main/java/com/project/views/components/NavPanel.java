package com.project.views.components;

import com.project.controller.MainControllerInterface;
import com.project.views.TestView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


public class NavPanel extends VerticalLayout {

    private Button mainPageButton;
    private Button catPageButton;
    private Button recentCourcesButton;
    private Button courseCalendarButton;
    private Button contactPageButton;

    private Button addCourseButton;

    public NavPanel(MainControllerInterface controllerInterface) {
        this.setClassName("nav-panel");

        mainPageButton = new Button("Главная страница");
        mainPageButton.addClickListener(click -> {
            mainPageButton.getUI().ifPresent(ui -> {
                ui.navigate("vaadin_project/main_page");
            });
        });
        mainPageButton.setClassName("nav-button");
        catPageButton = new Button("Категории");
        catPageButton.addClickListener(click -> {
        });
        catPageButton.setClassName("nav-button");
        recentCourcesButton = new Button("Новые тренинги");
        recentCourcesButton.addClickListener(click -> {
        });
        recentCourcesButton.setClassName("nav-button");
        courseCalendarButton = new Button("Запланированные курсы");
        courseCalendarButton.addClickListener(click -> {
        });
        courseCalendarButton.setClassName("nav-button");

        add(mainPageButton, catPageButton, recentCourcesButton, courseCalendarButton);

        Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();
        if (userAuthentication != null && userAuthentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            addCourseButton = new Button("Добавить курс");
            addCourseButton.addClickListener(click -> {
//                addCourseButton.getUI().ifPresent(ui -> {
//                    ui.navigate("vaadin_project/course_creation");
//                });
                Dialog dialog = new Dialog();
                dialog.getElement().setAttribute("aria-label", "Create new course");

                CourseCreationPanel courseCreationPanel = new CourseCreationPanel(controllerInterface, dialog);
                dialog.setModal(false);
                dialog.setDraggable(true);

                dialog.add(courseCreationPanel);
                dialog.open();
            });
            addCourseButton.setClassName("nav-button-admin");
            add(new Hr(), addCourseButton);
        }
    }

}