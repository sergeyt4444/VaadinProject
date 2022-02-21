package com.project.views.components;

import com.project.controller.MainControllerInterface;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


public class NavPanel extends VerticalLayout {

    private Button mainPageButton;
    private Button catPageButton;
    private Button recentCourcesButton;

    private Button addCategoryButton;
    private Button addCourseButton;

    public NavPanel(MainControllerInterface controllerInterface) {
        this.setClassName("nav-panel");

        mainPageButton = new Button("Main page");
        mainPageButton.addClickListener(click -> {
            mainPageButton.getUI().ifPresent(ui -> {
                ui.navigate("vaadin_project/main_page");
            });
        });
        mainPageButton.setClassName("nav-button");
        catPageButton = new Button("Categories");
        catPageButton.addClickListener(click -> {
        });
        catPageButton.setClassName("nav-button");
        recentCourcesButton = new Button("New trainings");
        recentCourcesButton.addClickListener(click -> {
        });
        recentCourcesButton.setClassName("nav-button");

        add(mainPageButton, catPageButton, recentCourcesButton);

        Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();
        if (userAuthentication != null && userAuthentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            addCategoryButton = new Button("Add category");
            addCategoryButton.addClickListener(click -> {
                Dialog dialog = new Dialog();

                CategoryCreationPanel categoryCreationPanel = new CategoryCreationPanel(controllerInterface, dialog);
                dialog.setModal(false);
                dialog.setDraggable(true);

                dialog.add(categoryCreationPanel);
                dialog.open();
            });
            addCategoryButton.setClassName("nav-button-admin");
            add(new Hr(), addCategoryButton);

            String rootCategoryId = VaadinSession.getCurrent().getAttribute("root category id").toString();
            if (rootCategoryId != null && !rootCategoryId.equals("0")) {
                addCourseButton = new Button("Add course");
                addCourseButton.addClickListener(click -> {
                    Dialog dialog = new Dialog();

                    CourseCreationPanel courseCreationPanel = new CourseCreationPanel(controllerInterface, dialog);
                    dialog.setModal(false);
                    dialog.setDraggable(true);

                    dialog.add(courseCreationPanel);
                    dialog.open();

                });
                addCourseButton.setClassName("nav-button-admin");
                add(addCourseButton);
            }
        }
    }

}