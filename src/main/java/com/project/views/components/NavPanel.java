package com.project.views.components;

import com.project.controller.AdminControllerInterface;
import com.project.controller.UserControllerInterface;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;


public class NavPanel extends VerticalLayout {

    private Button mainPageButton;
    private Button catPageButton;
    private Button recentCourcesButton;

    private Button addCategoryButton;
    private Button addCourseButton;
    private Button manageAttributesButton;

    public NavPanel(UserControllerInterface controllerInterface, AdminControllerInterface adminControllerInterface) {
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
            catPageButton.getUI().ifPresent(ui -> {
                ui.navigate("vaadin_project/categories");
            });
        });
        catPageButton.setClassName("nav-button");
        recentCourcesButton = new Button("Latest courses");
        recentCourcesButton.addClickListener(click -> {
            recentCourcesButton.getUI().ifPresent(ui -> {
                ui.navigate("vaadin_project/latest_courses");
            });
        });
        recentCourcesButton.setClassName("nav-button");

        add(mainPageButton, catPageButton, recentCourcesButton);

        Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();
        if (userAuthentication != null && userAuthentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            addCategoryButton = new Button("Add category");
            addCategoryButton.addClickListener(click -> {
                Dialog dialog = new Dialog();

                CategoryCreationPanel categoryCreationPanel = new CategoryCreationPanel(controllerInterface, adminControllerInterface, dialog);
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

                    CourseCreationPanel courseCreationPanel = new CourseCreationPanel(controllerInterface, adminControllerInterface, dialog);
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

    public void addAttributeManagementButton(UserControllerInterface controllerInterface, AdminControllerInterface adminControllerInterface) {
        Map<Integer, String> mappedCourse = (Map<Integer, String>) ComponentUtil.getData(UI.getCurrent(), "course");
        if (mappedCourse != null) {
            manageAttributesButton = new Button("Manage course");
            manageAttributesButton.addClickListener(click -> {
                Dialog dialog = new Dialog();
                CourseManagementPanel courseManagementPanel = new CourseManagementPanel(controllerInterface, adminControllerInterface, dialog);
                dialog.setModal(false);
                dialog.setDraggable(true);
                dialog.add(courseManagementPanel);
                dialog.open();
            });
            manageAttributesButton.setClassName("nav-button-admin");
            add(manageAttributesButton);
        }
    }

}