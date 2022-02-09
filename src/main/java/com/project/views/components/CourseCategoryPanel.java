package com.project.views.components;

import com.project.entity.AttrEnum;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.Map;



@CssImport("./styles/styles.css")
public class CourseCategoryPanel extends VerticalLayout {

    private Label courseNameLabel;
    private Label courseDescrLabel;

    public CourseCategoryPanel(Map<Integer, String> mappedObj) {
        addClassName("course-category-panel");
        courseNameLabel = new Label();
        courseNameLabel.addClassName("course-name-label");
        String courseName = mappedObj.get(AttrEnum.COURSE_NAME.getValue());
        String courseDescr = mappedObj.get(AttrEnum.COURSE_DESCRIPTION.getValue());
        courseNameLabel.getElement().setProperty("innerHTML", "<b>" + courseName + "</b>");
        courseDescrLabel = new Label(courseDescr);
        courseDescrLabel.setClassName("course-description-label");

        this.setAlignItems(Alignment.STRETCH);

        add(courseNameLabel, courseDescrLabel);
    }

}
