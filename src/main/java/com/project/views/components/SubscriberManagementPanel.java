package com.project.views.components;

import com.project.controller.ModeratorControllerInterface;
import com.project.controller.UserControllerInterface;
import com.project.entity.AttrEnum;
import com.project.entity.Obj;
import com.project.entity.ObjAttr;
import com.project.tools.AttributeTool;
import com.project.tools.MiscTool;
import com.project.tools.ObjectConverter;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.select.Select;

import java.util.*;

public class SubscriberManagementPanel extends FormLayout {

    private Grid<Map<Integer, String>> subscribersGrid;
    private Button submitButton;
    private Button cancelButton;

    public SubscriberManagementPanel(UserControllerInterface controllerInterface,
                                     ModeratorControllerInterface moderatorControllerInterface, Dialog dialog) {
        Map<Integer, String> mappedCourse = (Map<Integer, String>) ComponentUtil.getData(UI.getCurrent(), "course");
        this.setClassName("subscriber-management-panel");

        String subscribers = mappedCourse.get(AttrEnum.SUBSCRIBERS.getValue());
        List<String> subscriberList = Arrays.asList(subscribers.split(";"));
        if (subscriberList.contains("")) {
            subscriberList.remove("");
        }

        List<Map<Integer,String>> subscriberMappedObjects = new ArrayList<>();
        for (String subscriber: subscriberList) {
            subscriberMappedObjects.add(ObjectConverter
                    .convertObject(controllerInterface.getObjectById(Integer.parseInt(subscriber)).getBody()));
        }


        subscribersGrid = new Grid<>();
        subscribersGrid.setClassName("user-courses-grid");
        subscribersGrid.setItems(subscriberMappedObjects);

        subscribersGrid.addColumn(map -> ObjectConverter.getIdFromMappedObj(map)).setHeader("Id")
                .setAutoWidth(true).setFlexGrow(0);
        subscribersGrid.addColumn(map -> map.get(AttrEnum.USER_NAME.getValue())).setHeader("Username")
                .setResizable(true).setWidth("195px");

        Map<Integer, Select<String>> statusSelectTable = new HashMap<>();


        subscribersGrid.addComponentColumn(map -> {
            Select<String> statusSelect = new Select<>("None", "Failed", "Passed");
            statusSelect.setValue("None");
            String failedCoursesString = "";
            if (map.containsKey(AttrEnum.COURSES_FAILED.getValue()) && map.get(AttrEnum.COURSES_FAILED.getValue()) != null) {
                failedCoursesString = map.get(AttrEnum.COURSES_FAILED.getValue());
            }
            List<String> failedCourses = Arrays.asList(failedCoursesString.split(";"));
            if (failedCourses.contains(Integer.toString(ObjectConverter.getIdFromMappedObj(mappedCourse)))) {
                statusSelect.setValue("Failed");
            }
            String finishedCoursesString = "";
            if (map.containsKey(AttrEnum.COURSES_FINISHED.getValue()) && map.get(AttrEnum.COURSES_FINISHED.getValue()) != null) {
                finishedCoursesString = map.get(AttrEnum.COURSES_FINISHED.getValue());
            }
            List<String> finishedCourses = Arrays.asList(finishedCoursesString.split(";"));
            if (finishedCourses.contains(Integer.toString(ObjectConverter.getIdFromMappedObj(mappedCourse)))) {
                statusSelect.setValue("Passed");
            }
            statusSelectTable.put(ObjectConverter.getIdFromMappedObj(map), statusSelect);
            return statusSelect;
        }).setHeader("Status").setAutoWidth(true).setFlexGrow(0);

        subscribersGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        subscribersGrid.setHeightByRows(true);


        submitButton = new Button("Submit");
        submitButton.setClassName("course-creation-button");
        submitButton.addClickListener(click -> {
            List<Map<String,String>> mappedObjAttrList = new ArrayList<>();
            for (Map<Integer, String> subscriberMappedObj: subscriberMappedObjects) {
                String modifiedFailedCourses = "";
                String modifiedFinishedCourses = "";
                switch (statusSelectTable.get(ObjectConverter.getIdFromMappedObj(subscriberMappedObj)).getValue()) {
                    case "None": {
                        modifiedFailedCourses = MiscTool.removeNumFromStringList(subscriberMappedObj.get(AttrEnum.COURSES_FAILED.getValue()),
                                ObjectConverter.getIdFromMappedObj(mappedCourse));
                        modifiedFinishedCourses = MiscTool.removeNumFromStringList(subscriberMappedObj.get(AttrEnum.COURSES_FINISHED.getValue()),
                                ObjectConverter.getIdFromMappedObj(mappedCourse));
                        break;
                    }
                    case "Failed": {
                        modifiedFailedCourses = MiscTool.removeNumFromStringList(subscriberMappedObj.get(AttrEnum.COURSES_FAILED.getValue()),
                                ObjectConverter.getIdFromMappedObj(mappedCourse)) + Integer.toString(ObjectConverter.getIdFromMappedObj(mappedCourse));
                        modifiedFinishedCourses = MiscTool.removeNumFromStringList(subscriberMappedObj.get(AttrEnum.COURSES_FINISHED.getValue()),
                                ObjectConverter.getIdFromMappedObj(mappedCourse));
                        break;
                    }
                    case "Passed": {
                        modifiedFailedCourses = MiscTool.removeNumFromStringList(subscriberMappedObj.get(AttrEnum.COURSES_FAILED.getValue()),
                                ObjectConverter.getIdFromMappedObj(mappedCourse));
                        modifiedFinishedCourses = MiscTool.removeNumFromStringList(subscriberMappedObj.get(AttrEnum.COURSES_FINISHED.getValue()),
                                ObjectConverter.getIdFromMappedObj(mappedCourse)) + Integer.toString(ObjectConverter.getIdFromMappedObj(mappedCourse));
                        break;
                    }
                }
                mappedObjAttrList.add(AttributeTool.convertObjAttr("courses failed",
                        modifiedFailedCourses, ObjectConverter.getIdFromMappedObj(subscriberMappedObj)));
                mappedObjAttrList.add(AttributeTool.convertObjAttr("courses finished",
                        modifiedFinishedCourses, ObjectConverter.getIdFromMappedObj(subscriberMappedObj)));
            }
            controllerInterface.addUserCourseBulk(mappedObjAttrList);
            UI.getCurrent().getPage().reload();
        });

        cancelButton = new Button("Cancel");
        cancelButton.setClassName("course-creation-button");
        cancelButton.addClickListener(click -> {
            dialog.close();
        });

        this.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));
        this.setColspan(subscribersGrid, 2);
        this.setColspan(submitButton, 1);
        this.setColspan(cancelButton,1);
        add(subscribersGrid, submitButton, cancelButton);
    }

}
