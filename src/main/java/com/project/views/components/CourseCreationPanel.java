package com.project.views.components;

import com.project.controller.MainControllerInterface;
import com.project.entity.AttrEnum;
import com.project.entity.Attribute;
import com.project.tools.ObjectConverter;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextField;
import org.keycloak.KeycloakPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


@CssImport("./styles/styles.css")
public class CourseCreationPanel extends FormLayout {

    TextField courseNameField;
    TextField courseDescrField;
    Button submitCourse;
    Button closeDialog;

    public CourseCreationPanel(MainControllerInterface controllerInterface, Dialog dialog) {
        this.setClassName("course-creation-panel");

        courseNameField = new TextField("Название курса");
        courseNameField.addClassName("course-creation-tf");
        courseDescrField = new TextField("Описание курса");
        courseDescrField.addClassName("course-creation-tf");
        submitCourse = new Button("Создать курс");
        submitCourse.addClassName("course-creation-button");
        submitCourse.addClickListener(click -> {
            Map<Integer, String> courseAttrs = new HashMap<>();
            courseAttrs.put(AttrEnum.COURSE_NAME.getValue(), courseNameField.getValue());
            courseAttrs.put(AttrEnum.COURSE_DESCRIPTION.getValue(), courseDescrField.getValue());
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            KeycloakPrincipal principal = ((KeycloakPrincipal) authentication.getPrincipal());
            String username = principal.getKeycloakSecurityContext().getToken().getPreferredUsername();
            courseAttrs.put(AttrEnum.CREATOR.getValue(), username);
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            courseAttrs.put(AttrEnum.CREATION_DATE.getValue(), dateFormat.format(date));
            if (ObjectConverter.validateMappedObject(courseAttrs)) {
                controllerInterface.createObj(courseAttrs);
                Notification.show("Курс создан");
            }
            else {
                Notification notification = new Notification("Некорректные значения атрибутов");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.open();
            }
        });

        closeDialog = new Button("Закрыть");
        closeDialog.addClassName("course-creation-button");
        closeDialog.addClickListener(click -> {
            dialog.close();
        });

        this.setResponsiveSteps(new ResponsiveStep("0", 2));
        this.setColspan(courseNameField, 2);
        this.setColspan(courseDescrField, 2);
        add(courseNameField, courseDescrField, submitCourse, closeDialog);
    }

}
