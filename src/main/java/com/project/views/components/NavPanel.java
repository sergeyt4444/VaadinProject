package com.project.views.components;

import com.project.controller.AdminControllerInterface;
import com.project.controller.UserControllerInterface;
import com.project.entity.AttrEnum;
import com.project.entity.Obj;
import com.project.tools.AttributeTool;
import com.project.tools.MiscTool;
import com.project.tools.ObjectConverter;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class NavPanel extends VerticalLayout {

    private Button mainPageButton;
    private Button catPageButton;
    private Button recentCourcesButton;

    private Button addCategoryButton;
    private Button addCourseButton;
    private Button manageAttributesButton;
    private Button courseDeletionButton;
    private Button categoryDeletionButton;

    private ConfirmDialog confirmDeleteCourse;
    private ConfirmDialog confirmDeleteCategory;

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

        add(mainPageButton, catPageButton, recentCourcesButton, new Hr());


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
            add(addCategoryButton);

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

    public void addCourseDeletionButton(UserControllerInterface controllerInterface, AdminControllerInterface adminControllerInterface) {
        Map<Integer, String> mappedCourse = (Map<Integer, String>) ComponentUtil.getData(UI.getCurrent(), "course");
        if (mappedCourse != null) {
            courseDeletionButton = new Button("Delete course");
            confirmDeleteCourse = new ConfirmDialog();
            confirmDeleteCourse.setText("Are you sure?");
            confirmDeleteCourse.setCancelable(true);
            confirmDeleteCourse.addCancelListener(cancelEvent -> {
            });
            confirmDeleteCourse.addConfirmListener(confirmEvent -> {
                deleteCourse(controllerInterface, adminControllerInterface, mappedCourse);
                courseDeletionButton.getUI().ifPresent(ui -> {
                    ui.navigate("vaadin_project/main_page");
                });
            });
            courseDeletionButton.addClickListener(click -> {
                confirmDeleteCourse.open();
            });
            courseDeletionButton.setClassName("nav-button-admin");
            add(courseDeletionButton);
        }
    }

    public void addCategoryDeletionButton(UserControllerInterface controllerInterface, AdminControllerInterface adminControllerInterface) {
        String rootCategoryId = VaadinSession.getCurrent().getAttribute("root category id").toString();
        Obj categoryObj = controllerInterface.getObjectById(Integer.parseInt(rootCategoryId)).getBody();
        if (categoryObj != null) {
            categoryDeletionButton = new Button("Delete category");
            confirmDeleteCategory = new ConfirmDialog();
            confirmDeleteCategory.setText("Are you sure? Its contents will be deleted as well");
            confirmDeleteCategory.setCancelable(true);
            confirmDeleteCategory.addCancelListener(cancelEvent -> {
            });
            confirmDeleteCategory.addConfirmListener(confirmEvent -> {
                deleteCategory(controllerInterface, adminControllerInterface, categoryObj.getObjId());
                categoryDeletionButton.getUI().ifPresent(ui -> {
                    ui.navigate("vaadin_project/main_page");
                });
            });
            categoryDeletionButton.addClickListener(click -> {
                confirmDeleteCategory.open();
            });

            categoryDeletionButton.setClassName("nav-button-admin");
            add(categoryDeletionButton);

        }
    }

    private void deleteCourse(UserControllerInterface controllerInterface, AdminControllerInterface adminControllerInterface,
                             Map<Integer, String> mappedCourse) {
        String subscribers = mappedCourse.get(AttrEnum.SUBSCRIBERS.getValue());
        List<String> subList = Arrays.asList(subscribers.split(";"));
        for (String subscriber: subList) {
            if (subscriber.equals("")) {
                continue;
            }
            int subId = Integer.parseInt(subscriber);
            Obj userObj = controllerInterface.getObjectById(subId).getBody();
            Map<Integer, String> mappedUser = ObjectConverter.convertObject(userObj);
            String updatedUserCourses = MiscTool.removeNumFromStringList(mappedUser.get(AttrEnum.USER_COURSES.getValue()),
                    ObjectConverter.getIdFromMappedObj(mappedCourse));
            Map<String, String> mappedUserCourses = AttributeTool.convertObjAttr("user courses",
                    updatedUserCourses, subId);
            controllerInterface.addUserCourse(mappedUserCourses);
        }
        adminControllerInterface.deleteObj(ObjectConverter.getIdFromMappedObj(mappedCourse));
    }

    private void deleteCategory(UserControllerInterface controllerInterface, AdminControllerInterface adminControllerInterface, Integer id) {
        List<Obj> courses = controllerInterface.getCourses(id,1,Integer.MAX_VALUE).getBody();
        List<Obj> subcategories = controllerInterface.getSubCategories(id).getBody();
        for (Obj course: courses) {
            deleteCourse(controllerInterface, adminControllerInterface, ObjectConverter.convertObject(course));
        }
        for (Obj subcategory: subcategories) {
            deleteCategory(controllerInterface, adminControllerInterface, subcategory.getObjId());
        }
        adminControllerInterface.deleteObj(id);
    }

}