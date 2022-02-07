package com.project.views.components;


import com.project.entity.Obj;
import com.project.entity.ObjAttr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class CourseCategoryPanel extends VerticalLayout {

    private Label courseNameLabel;
    private Label courseDescrLabel;

    public CourseCategoryPanel(Obj obj) {
        this.getStyle().set("background-color", "#f0f0f0");
        courseNameLabel = new Label();
        courseNameLabel.getStyle().set("font-size", "28px");
        courseNameLabel.getStyle().set("background-color", "#d8d8d8");
        String courseName = "";
        String courseDescr = "";
        for (ObjAttr objAttr: obj.getObjAttrs()) {
            if (objAttr.getAttribute().getAttrName().equals("course name")) {
                courseName = objAttr.getValue();
            }
        }
        for (ObjAttr objAttr: obj.getObjAttrs()) {
            if (objAttr.getAttribute().getAttrName().equals("course description")) {
                courseDescr = objAttr.getValue();
            }
        }
        courseNameLabel.getElement().setProperty("innerHTML", "<b>" + courseName + "</b>");

        courseDescrLabel = new Label(courseDescr);
        courseDescrLabel.getStyle().set("font-size", "20px");
        courseDescrLabel.setMinHeight("52px");
        courseDescrLabel.setMaxWidth("270px");

        this.setAlignItems(Alignment.STRETCH);

        add(courseNameLabel, courseDescrLabel);
    }

}
