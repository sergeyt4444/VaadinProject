package com.project.views.components;


import com.project.entity.Obj;
import com.project.entity.ObjAttr;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@CssImport("./styles/styles.css")
public class CourseCategoryPanel extends VerticalLayout {

    private Label courseNameLabel;
    private Label courseDescrLabel;

    public CourseCategoryPanel(Obj obj) {
        addClassName("course-category-panel");
        courseNameLabel = new Label();
        courseNameLabel.addClassName("course-name-label");
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
        courseDescrLabel.setClassName("course-description-label");

        this.setAlignItems(Alignment.STRETCH);

        add(courseNameLabel, courseDescrLabel);
    }

}
