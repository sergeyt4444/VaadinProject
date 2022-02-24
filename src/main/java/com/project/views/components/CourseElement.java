package com.project.views.components;

import com.project.entity.AttrEnum;
import com.project.tools.ObjectConverter;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.QueryParameters;

import java.util.HashMap;
import java.util.Map;

@CssImport("./styles/styles.css")
public class CourseElement extends VerticalLayout {

    private Label courseNameLabel;
    private Label courseDescrLabel;

    public CourseElement(Map<Integer, String> mappedObj) {
        addClassName("course-element");
        courseNameLabel = new Label();
        courseNameLabel.addClassName("course-name-label");
        String courseName = mappedObj.get(AttrEnum.COURSE_NAME.getValue());
        String courseDescr = mappedObj.get(AttrEnum.COURSE_DESCRIPTION.getValue());
        courseNameLabel.getElement().setProperty("innerHTML", "<b>" + courseName + "</b>");
        courseDescrLabel = new Label(courseDescr);
        courseDescrLabel.setClassName("course-description-label");

        this.setAlignItems(Alignment.STRETCH);
        addClickListener(click -> {
            getUI().ifPresent(ui -> {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("id", Integer.toString(ObjectConverter.getIdFromMappedObj(mappedObj)));
                ui.navigate("vaadin_project/course", QueryParameters.simple(parameters));
            });
        });

        add(courseNameLabel, courseDescrLabel);
    }
}
