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
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RestartCoursePanel extends FormLayout {

    private DatePicker startDateDPicker;
    private Button submitButton;
    private Button cancelButton;

    public RestartCoursePanel(UserControllerInterface controllerInterface,
                              ModeratorControllerInterface moderatorControllerInterface, Dialog dialog) {
        Map<Integer, String> mappedCourse = (Map<Integer, String>) ComponentUtil.getData(UI.getCurrent(), "course");
        this.setClassName("restart-course-panel");

        startDateDPicker = new DatePicker("New restart date");
        startDateDPicker.setValue(LocalDate.now());
        startDateDPicker.setMin(LocalDate.now());
        startDateDPicker.setMax(LocalDate.now().plusYears(4));
        startDateDPicker.addClassName("primary-attr-dpicker");

        submitButton = new Button("Restart course");
        submitButton.setClassName("course-creation-button");
        submitButton.addClickListener(click -> {
            Obj obj = controllerInterface.getObjectById(ObjectConverter.getIdFromMappedObj(mappedCourse)).getBody();
            for (ObjAttr objAttr: obj.getObjAttrs()) {
                if (objAttr.getAttribute().getAttrId() > AttributeTool.PRIMARY_ATTRIBUTE_ID_SPACE) {
                    moderatorControllerInterface.deleteObjAttr(objAttr.getObjAttrId());
                }
            }
            List<String> subscribers = Arrays.asList(mappedCourse.get(AttrEnum.SUBSCRIBERS.getValue()).split(";"));
            for (String subscriber: subscribers) {
                if (subscriber.equals("")) {
                    continue;
                }
                Obj user = controllerInterface.getObjectById(Integer.parseInt(subscriber)).getBody();
                Map<Integer, String> mappedUser = ObjectConverter.convertObject(user);
                String userCourses = mappedUser.get(AttrEnum.USER_COURSES.getValue());
                String updatedUserCourses = MiscTool.removeNumFromStringList(userCourses, ObjectConverter.getIdFromMappedObj(mappedCourse));
                moderatorControllerInterface.changeObjAttr(AttributeTool.convertObjAttr("user courses",
                                updatedUserCourses, ObjectConverter.getIdFromMappedObj(mappedUser)));
            }
            moderatorControllerInterface.changeObjAttr(AttributeTool.convertObjAttr(
                    "start date",
                    startDateDPicker.getValue().toString(), ObjectConverter.getIdFromMappedObj(mappedCourse)));
            moderatorControllerInterface.changeObjAttr(AttributeTool.convertObjAttr(
                    AttributeTool.REQ_ATTRIBUTES.get(AttrEnum.CURRENT_PARTICIPANTS.getValue()),
                    "0", ObjectConverter.getIdFromMappedObj(mappedCourse)));
            moderatorControllerInterface.changeObjAttr(AttributeTool.convertObjAttr(
                    AttributeTool.REQ_ATTRIBUTES.get(AttrEnum.SUBSCRIBERS.getValue()),
                    "", ObjectConverter.getIdFromMappedObj(mappedCourse)));
            UI.getCurrent().getPage().reload();
        });

        cancelButton = new Button("Cancel");
        cancelButton.setClassName("course-creation-button");
        cancelButton.addClickListener(click -> {
            dialog.close();
        });

        this.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));
        this.setColspan(startDateDPicker, 2);
        this.setColspan(submitButton, 1);
        this.setColspan(cancelButton,1);
        add(startDateDPicker, submitButton, cancelButton);
    }

}
