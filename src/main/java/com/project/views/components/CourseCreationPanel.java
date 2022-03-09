package com.project.views.components;

import com.project.controller.AdminControllerInterface;
import com.project.controller.UserControllerInterface;
import com.project.entity.AttrEnum;
import com.project.tools.ObjectConverter;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.server.VaadinSession;
import org.keycloak.KeycloakPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.Map;

public class CourseCreationPanel extends CourseManipulationPanel {

    private Button submitCourse;
    private Button closeDialog;

    public CourseCreationPanel(UserControllerInterface controllerInterface,
                               AdminControllerInterface adminControllerInterface, Dialog dialog) {
        this.setClassName("course-creation-panel");

        submitCourse = new Button("Create course");
        submitCourse.addClassName("course-creation-button");
        submitCourse.addClickListener(click -> {
            Map<Integer, String> courseAttrs = new HashMap<>();
            courseAttrs.put(AttrEnum.COURSE_NAME.getValue(), courseNameField.getValue());
            courseAttrs.put(AttrEnum.COURSE_DESCRIPTION.getValue(), courseDescrField.getValue());
            courseAttrs.put(AttrEnum.PARTICIPANTS_REQUIRED.getValue(), minParticipantsField.getValue().toString());
            courseAttrs.put(AttrEnum.CURRENT_PARTICIPANTS.getValue(), "0");
            courseAttrs.put(AttrEnum.DIFFICULTY.getValue(), difficultySelect.getValue());
            courseAttrs.put(AttrEnum.LANGUAGE.getValue(), langSelect.getValue());
            courseAttrs.put(AttrEnum.FORMAT.getValue(), formatSelect.getValue());
            courseAttrs.put(AttrEnum.START_DATE.getValue(), startDateDPicker.getValue().toString());
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            KeycloakPrincipal principal = ((KeycloakPrincipal) authentication.getPrincipal());
            String username = principal.getKeycloakSecurityContext().getToken().getGivenName();
            courseAttrs.put(AttrEnum.CREATOR.getValue(), username);

            String rootCategoryId = VaadinSession.getCurrent().getAttribute("root category id").toString();
            if (rootCategoryId != null) {
                courseAttrs.put(AttrEnum.PARENT_ID.getValue(), rootCategoryId);
            }
            else {
                courseAttrs.put(AttrEnum.PARENT_ID.getValue(), "0");
            }

            if (ObjectConverter.validateMappedObject(courseAttrs)) {
                adminControllerInterface.createCourse(courseAttrs);
                UI.getCurrent().getPage().reload();
                Notification notification = new Notification("Course has been created");
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

        this.setColspan(submitCourse,3);
        this.setColspan(closeDialog,3);
        add(submitCourse, closeDialog);
    }
}
