package com.project.views.components;

import com.project.controller.MainControllerInterface;
import com.project.entity.AttrEnum;
import com.project.tools.AttributeTool;
import com.project.tools.ObjectConverter;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.IDToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CourseCreationPanel extends FormLayout {

    TextField courseNameField;
    TextField courseDescrField;
    IntegerField minParticipantsField;
    Select<String> difficultySelect;
    Select<String> langSelect;
    Select<String> formatSelect;
    DatePicker startDateDPicker;
    Button submitCourse;
    Button closeDialog;

    public CourseCreationPanel(MainControllerInterface controllerInterface, Dialog dialog) {
        this.setClassName("course-creation-panel");

        courseNameField = new TextField("Course name");
        courseNameField.addClassName("course-creation-tf");
        courseDescrField = new TextField("Course description");
        courseDescrField.addClassName("course-creation-tf");

        minParticipantsField = new IntegerField();
        minParticipantsField.setLabel("Required № of participants");
        minParticipantsField.setValue(10);
        minParticipantsField.setMin(1);
        minParticipantsField.setMax(250);
        minParticipantsField.addClassName("number-field");
        minParticipantsField.setHasControls(true);

        difficultySelect = new Select<>();
        difficultySelect.setItems(AttributeTool.getDifficulties());
        difficultySelect.setLabel("Difficulty");
        difficultySelect.setValue(AttributeTool.getDifficulties().get(0));

        langSelect = new Select<>();
        langSelect.setItems(AttributeTool.getLanguages());
        langSelect.setLabel("Language");
        langSelect.setValue(AttributeTool.getLanguages().get(0));

        formatSelect = new Select<>();
        formatSelect.setItems(AttributeTool.getFormats());
        formatSelect.setLabel("Format");
        formatSelect.setValue(AttributeTool.getFormats().get(0));

        startDateDPicker = new com.vaadin.flow.component.datepicker.DatePicker();
        startDateDPicker.setLabel("Start date");
        startDateDPicker.setValue(LocalDate.now());
        startDateDPicker.setMin(LocalDate.now());
        startDateDPicker.setMax(LocalDate.now().plusYears(4));


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
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
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
                controllerInterface.createCourse(courseAttrs);
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

        this.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));
        this.setColspan(courseNameField, 2);
        this.setColspan(courseDescrField, 2);
        this.setColspan(difficultySelect, 2);
        this.setColspan(langSelect, 2);
        this.setColspan(formatSelect, 2);
        this.setColspan(startDateDPicker, 2);
        add(courseNameField, courseDescrField, minParticipantsField, difficultySelect,
                langSelect, formatSelect, startDateDPicker, submitCourse, closeDialog);
    }
}
