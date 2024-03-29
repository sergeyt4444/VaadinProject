package com.project.views.components;

import com.project.controller.AdminControllerInterface;
import com.project.controller.UserControllerInterface;
import com.project.entity.AttrEnum;
import com.project.entity.Obj;
import com.project.tools.AttributeTool;
import com.project.tools.ObjectConverter;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


public class CourseManagementPanel extends CourseManipulationPanel {

    private Button addRequirement;
    private VerticalLayout requirementsLayout;
    private Button submitAttributes;
    private Button closeDialog;

    public CourseManagementPanel(UserControllerInterface controllerInterface,
                                 AdminControllerInterface adminControllerInterface, Dialog dialog) {
        Map<Integer, String> mappedCourse = (Map<Integer, String>) ComponentUtil.getData(UI.getCurrent(), "course");

        requirementsLayout = new VerticalLayout();
        String formerRequirements = mappedCourse.get(AttrEnum.REQUIREMENTS.getValue());
        if (formerRequirements != null && !formerRequirements.equals(""))
        {
            List<String> formerRequirementList = Arrays.asList(formerRequirements.split(";"));
            if (formerRequirementList.contains("")) {
                formerRequirementList.remove("");
            }
            for (String formerRequirement:formerRequirementList) {
                RequirementElement requirementElement = new RequirementElement(controllerInterface, mappedCourse);
                requirementsLayout.add(requirementElement);
                requirementElement.setRequirement(Integer.parseInt(formerRequirement));
            }
        }


        addRequirement = new Button("Add requirement");
        addRequirement.addClassName("course-creation-button");
        addRequirement.addClickListener(click -> {
            requirementsLayout.add(new RequirementElement(controllerInterface, mappedCourse));
        });


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
            Obj upToDatecourse = controllerInterface.getObjectById(ObjectConverter.getIdFromMappedObj(mappedCourse)).getBody();
            if (upToDatecourse != null) {
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
                mappedObjAttrs.add(AttributeTool.convertObjAttr(AttributeTool.REQ_ATTRIBUTES.get(AttrEnum.REQUIREMENTS.getValue()),
                        requirements.toString(), ObjectConverter.getIdFromMappedObj(mappedCourse)));
                adminControllerInterface.editCourse(mappedObjAttrs);
            }
            UI.getCurrent().getPage().reload();
        });


        closeDialog = new Button("Exit");
        closeDialog.addClassName("course-creation-button");
        closeDialog.addClickListener(click -> {
            dialog.close();
        });
        this.setColspan(addRequirement, 6);
        this.setColspan(requirementsLayout, 6);
        this.setColspan(submitAttributes,3);
        this.setColspan(closeDialog,3);
        add(addRequirement, requirementsLayout,submitAttributes, closeDialog);
    }

}
