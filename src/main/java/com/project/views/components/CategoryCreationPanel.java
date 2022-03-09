package com.project.views.components;

import com.project.controller.AdminControllerInterface;
import com.project.controller.UserControllerInterface;
import com.project.entity.AttrEnum;
import com.project.tools.ObjectConverter;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;
import org.keycloak.KeycloakPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@CssImport("./styles/styles.css")
public class CategoryCreationPanel extends FormLayout {

    private TextField categoryNameField;
    private TextField categoryDescrField;
    private Button submitCategory;
    private Button closeDialog;

    public CategoryCreationPanel(UserControllerInterface controllerInterface,
                                 AdminControllerInterface adminControllerInterface, Dialog dialog) {
        this.setClassName("course-creation-panel");

        categoryNameField = new TextField("Category name");
        categoryNameField.addClassName("course-creation-tf");
        categoryDescrField = new TextField("Category description");
        categoryDescrField.addClassName("course-creation-tf");
        submitCategory = new Button("Create category");
        submitCategory.addClassName("course-creation-button");
        submitCategory.addClickListener(click -> {
            Map<Integer, String> courseAttrs = new HashMap<>();
            courseAttrs.put(AttrEnum.COURSE_NAME.getValue(), categoryNameField.getValue());
            courseAttrs.put(AttrEnum.COURSE_DESCRIPTION.getValue(), categoryDescrField.getValue());
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            KeycloakPrincipal principal = ((KeycloakPrincipal) authentication.getPrincipal());
            String username = principal.getKeycloakSecurityContext().getToken().getPreferredUsername();
            courseAttrs.put(AttrEnum.CREATOR.getValue(), username);
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            courseAttrs.put(AttrEnum.START_DATE.getValue(), dateFormat.format(date));

            String rootCategoryId = VaadinSession.getCurrent().getAttribute("root category id").toString();
            if (rootCategoryId != null) {
                courseAttrs.put(AttrEnum.PARENT_ID.getValue(), rootCategoryId);
            }
            else {
                courseAttrs.put(AttrEnum.PARENT_ID.getValue(), "0");
            }

            if (ObjectConverter.validateMappedObject(courseAttrs)) {
                adminControllerInterface.createCategory(courseAttrs);
                UI.getCurrent().getPage().reload();
                Notification notification = new Notification("Category has been created");
                notification.setPosition(Notification.Position.TOP_END);
                notification.open();
            }
            else {
                Notification notification = new Notification("Invalid attribute values");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setPosition(Notification.Position.TOP_END);
                notification.open();
            }
        });

        closeDialog = new Button("Exit");
        closeDialog.addClassName("course-creation-button");
        closeDialog.addClickListener(click -> {
            dialog.close();
        });

        this.setResponsiveSteps(new ResponsiveStep("0", 2));
        this.setColspan(categoryNameField, 2);
        this.setColspan(categoryDescrField, 2);
        add(categoryNameField, categoryDescrField, submitCategory, closeDialog);
    }

}
