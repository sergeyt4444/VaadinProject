package com.project.views.components;

import com.project.controller.MainControllerInterface;
import com.project.entity.AttrEnum;
import com.project.tools.AttributeTool;
import com.project.tools.ObjectConverter;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.server.VaadinSession;
import org.keycloak.KeycloakPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CourseManagementPanel extends CourseManipulationPanel {

    private Button submitAttributes;
    private Button closeDialog;

    public CourseManagementPanel(MainControllerInterface controllerInterface, Dialog dialog) {
        Map<Integer, String> mappedCourse = (Map<Integer, String>) ComponentUtil.getData(UI.getCurrent(), "course");

        courseNameField.setValue(mappedCourse.get(AttrEnum.COURSE_NAME.getValue()));
        courseDescrField.setValue(mappedCourse.get(AttrEnum.COURSE_DESCRIPTION.getValue()));
        minParticipantsField.setValue(Integer.parseInt(mappedCourse.get(AttrEnum.PARTICIPANTS_REQUIRED.getValue())));
        difficultySelect.setValue(mappedCourse.get(AttrEnum.DIFFICULTY.getValue()));
        langSelect.setValue(mappedCourse.get(AttrEnum.LANGUAGE.getValue()));
        formatSelect.setValue(mappedCourse.get(AttrEnum.FORMAT.getValue()));
        startDateDPicker.setValue(LocalDate.parse(mappedCourse.get(AttrEnum.START_DATE.getValue())));

        submitAttributes = new Button("Submit");
        submitAttributes.addClassName("course-creation-button");
        submitAttributes.addClickListener(click -> {
            List<Map<String, String>> mappedObjAttrs = new ArrayList<>();
            mappedObjAttrs.add(AttributeTool.convertObjAttr(AttributeTool.REQ_ATTRIBUTES.get(AttrEnum.COURSE_NAME.getValue()),
                    courseNameField.getValue(), ObjectConverter.getIdFromMappedObj(mappedCourse)));
            mappedObjAttrs.add(AttributeTool.convertObjAttr(AttributeTool.REQ_ATTRIBUTES.get(AttrEnum.COURSE_DESCRIPTION.getValue()),
                    courseDescrField.getValue(), ObjectConverter.getIdFromMappedObj(mappedCourse)));
            mappedObjAttrs.add(AttributeTool.convertObjAttr(AttributeTool.REQ_ATTRIBUTES.get(AttrEnum.PARTICIPANTS_REQUIRED.getValue()),
                    Integer.toString(minParticipantsField.getValue()), ObjectConverter.getIdFromMappedObj(mappedCourse)));
            mappedObjAttrs.add(AttributeTool.convertObjAttr(AttributeTool.REQ_ATTRIBUTES.get(AttrEnum.DIFFICULTY.getValue()),
                    difficultySelect.getValue(), ObjectConverter.getIdFromMappedObj(mappedCourse)));
            mappedObjAttrs.add(AttributeTool.convertObjAttr(AttributeTool.REQ_ATTRIBUTES.get(AttrEnum.LANGUAGE.getValue()),
                    langSelect.getValue(), ObjectConverter.getIdFromMappedObj(mappedCourse)));
            mappedObjAttrs.add(AttributeTool.convertObjAttr(AttributeTool.REQ_ATTRIBUTES.get(AttrEnum.FORMAT.getValue()),
                    formatSelect.getValue(), ObjectConverter.getIdFromMappedObj(mappedCourse)));
            mappedObjAttrs.add(AttributeTool.convertObjAttr(AttributeTool.REQ_ATTRIBUTES.get(AttrEnum.START_DATE.getValue()),
                    startDateDPicker.getValue().toString(), ObjectConverter.getIdFromMappedObj(mappedCourse)));

            controllerInterface.editCourse(mappedObjAttrs);

            UI.getCurrent().getPage().reload();
        });


        closeDialog = new Button("Exit");
        closeDialog.addClassName("course-creation-button");
        closeDialog.addClickListener(click -> {
            dialog.close();
        });

        this.setColspan(submitAttributes,3);
        this.setColspan(closeDialog,3);
        add(submitAttributes, closeDialog);
    }

}