package com.project.views.components;

import com.project.controller.AdminControllerInterface;
import com.project.controller.UserControllerInterface;
import com.project.entity.AttrEnum;
import com.project.tools.AttributeTool;
import com.project.tools.ObjectConverter;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinSession;
import org.keycloak.KeycloakPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;
import java.util.stream.Collectors;

public class CourseCreationPanel extends CourseManipulationPanel {

    private Button addRequirement;
    private VerticalLayout requirementsLayout;
    private Button submitCourse;
    private Button closeDialog;

    public CourseCreationPanel(UserControllerInterface controllerInterface,
                               AdminControllerInterface adminControllerInterface, Dialog dialog) {
        this.setClassName("course-creation-panel");

        requirementsLayout = new VerticalLayout();

        addRequirement = new Button("Add requirement");
        addRequirement.addClassName("course-creation-button");
        addRequirement.addClickListener(click -> {
            requirementsLayout.add(new RequirementElement(controllerInterface, null));
        });

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
            courseAttrs.put(AttrEnum.SUBSCRIBERS.getValue(), "");
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            KeycloakPrincipal principal = ((KeycloakPrincipal) authentication.getPrincipal());
            String username = principal.getKeycloakSecurityContext().getToken().getGivenName();
            courseAttrs.put(AttrEnum.CREATOR.getValue(), username);

            StringBuilder requirements = new StringBuilder("");
            List<Component> componentList = requirementsLayout.getChildren().collect(Collectors.toList());
            Set<String> requirementsSet = new HashSet<>();
            for (Component requirement: componentList) {
                RequirementElement requirementElement = (RequirementElement) requirement;
                if (requirementElement.getValue()!=null)
                {
                    requirementsSet.add(Integer.toString(requirementElement.getValue()));
                }
            }
            for (String reqiurementString: requirementsSet) {
                requirements.append(reqiurementString).append(";");
            }
            courseAttrs.put(AttrEnum.REQUIREMENTS.getValue(), requirements.toString());

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
        this.setColspan(addRequirement, 6);
        this.setColspan(requirementsLayout, 6);
        this.setColspan(submitCourse,3);
        this.setColspan(closeDialog,3);
        add(addRequirement, requirementsLayout, submitCourse, closeDialog);
    }
}
