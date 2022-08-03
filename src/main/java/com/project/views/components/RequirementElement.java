package com.project.views.components;

import com.project.controller.UserControllerInterface;
import com.project.entity.AttrEnum;
import com.project.entity.Obj;
import com.project.entity.ObjectTypeEnum;
import com.project.tools.ObjectConverter;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RequirementElement extends HorizontalLayout {

    private UserControllerInterface controllerInterface;
    private ComboBox<Map<Integer, String>> requiredCourseComboBox;
    private Button removeRequirement;

    public RequirementElement(UserControllerInterface controllerInterface, Map<Integer, String> mappedCourse) {
        this.controllerInterface = controllerInterface;
        requiredCourseComboBox = new ComboBox<>();
        List<Map<Integer, String>> courses = ObjectConverter.convertListOfObjects(controllerInterface.getAllCourses().getBody());
        courses.remove(mappedCourse);
        List<Map<Integer, String>> coursesForRemoval = new ArrayList<>();
        if (mappedCourse != null) {
            for (Map<Integer, String> course: courses) {
                if (course.get(AttrEnum.REQUIREMENTS.getValue()) != null && !course.get(AttrEnum.REQUIREMENTS.getValue()).equals("")) {
                    List<String> requirementList = Arrays.asList(course.get(AttrEnum.REQUIREMENTS.getValue()).split(";"));
                    if (requirementList.contains(Integer.toString(ObjectConverter.getIdFromMappedObj(mappedCourse)))) {
                        coursesForRemoval.add(course);
                    }
                }
            }
            courses.removeAll(coursesForRemoval);

            if (mappedCourse.get(AttrEnum.REQUIREMENTS.getValue()) != null &&
                    !mappedCourse.get(AttrEnum.REQUIREMENTS.getValue()).equals("")) {
                List<String> requirementList = Arrays.asList(mappedCourse.get(AttrEnum.REQUIREMENTS.getValue()).split(";"));
                if (requirementList.contains("")) {
                    requirementList.remove("");
                }
                for (String dupRequirement: requirementList){
                    Obj cycleRequirementCourse = controllerInterface.getObjectById(Integer.parseInt(dupRequirement)).getBody();
                    Map<Integer, String> mappedCycleRequirementCourse = ObjectConverter.convertObject(cycleRequirementCourse);
                    if (courses.contains(mappedCycleRequirementCourse)) {
                        courses.remove(mappedCycleRequirementCourse);
                    }
                }
            }
        }
        requiredCourseComboBox.setItems(courses);
        requiredCourseComboBox.setItemLabelGenerator(map -> map.get(AttrEnum.COURSE_NAME.getValue()));
        requiredCourseComboBox.setPlaceholder("Select requirement");

        Icon crossIcon = new Icon(VaadinIcon.TRASH);
        removeRequirement = new Button(crossIcon);
        removeRequirement.addClickListener(buttonClickEvent -> {
            VerticalLayout parentLayout = (VerticalLayout) this.getParent().get();
            parentLayout.remove(this);
        });
        add(requiredCourseComboBox, removeRequirement);
    }

    public Integer getValue() {
        if (requiredCourseComboBox.getValue() == null) {
            return null;
        }
        return ObjectConverter.getIdFromMappedObj(requiredCourseComboBox.getValue());
    }

    public void setRequirement(int requirementID) {
        Obj course = controllerInterface.getObjectById(requirementID).getBody();
        if (course != null && course.getObjectType().getObjTypesId() == ObjectTypeEnum.COURSE.getValue()) {
            requiredCourseComboBox.setValue(ObjectConverter.convertObject(course));
        }
        else {
            removeRequirement.click();
        }
    }

}
