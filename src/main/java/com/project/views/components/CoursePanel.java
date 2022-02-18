package com.project.views.components;

import com.project.entity.AttrEnum;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import java.util.HashMap;
import java.util.Map;

@CssImport("./styles/styles.css")
public class CoursePanel extends VerticalLayout {

    private H1 nameHeader;
    private HorizontalLayout creationInfoLayout;
    private Label creatorLabel;
    private Label creationDateLabel;
    private HorizontalLayout courseDataLayout;
    private VerticalLayout courseDescrLayout;
    private H2 descriptionHeader;
    private TextArea descriptionTA;
    private VerticalLayout attributeLayout;
    private Label attributeLabel;
    private Button addAttributeButton;
    private Button joinCourseButton;

    public CoursePanel(Map<Integer, String> mappedObj) {
        this.setAlignItems(Alignment.CENTER);
        this.setJustifyContentMode(JustifyContentMode.START);
        this.addClassName("course-panel");

        nameHeader = new H1(mappedObj.get(AttrEnum.COURSE_NAME.getValue()));
        nameHeader.setClassName("name-header");

        creatorLabel = new Label("Created by: " + mappedObj.get(AttrEnum.CREATOR.getValue()));
        creatorLabel.setClassName("course-info-label");

        creationDateLabel = new Label(mappedObj.get(AttrEnum.CREATION_DATE.getValue()));
        creationDateLabel.setClassName("course-info-label");

        creationInfoLayout = new HorizontalLayout(creatorLabel, creationDateLabel);
        creationInfoLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        creationInfoLayout.setClassName("creation-info-layout");

        descriptionHeader = new H2("Course description");
        descriptionHeader.setClassName("description-header");

        descriptionTA = new TextArea();
        descriptionTA.setValue(mappedObj.get(AttrEnum.COURSE_DESCRIPTION.getValue()));
        descriptionTA.setClassName("description-ta");

        courseDescrLayout = new VerticalLayout(descriptionHeader, descriptionTA);
        courseDescrLayout.setClassName("course-descr-layout");
        courseDescrLayout.setAlignItems(Alignment.CENTER);

        attributeLayout = new VerticalLayout();
        attributeLayout.setClassName("attribute-layout");
        attributeLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        attributeLayout.setAlignItems(Alignment.START);

        attributeLabel = new Label("Other attributes: ");
        attributeLabel.setClassName("attribute-label");
        attributeLayout.add(attributeLabel);

        //hiding already known attributes
        Map<Integer, String> etcAttrs = new HashMap<>(mappedObj);
        for (int i = -1; i <= 6; i++) {
            etcAttrs.remove(i);
        }

        for (Map.Entry<Integer, String> attr: etcAttrs.entrySet()) {
            attributeLayout.add(new Label("Attribute id: " + attr.getKey() + ", attribute value: " + attr.getValue()));
        }

        addAttributeButton= new Button("Create attribute");
        addAttributeButton.setClassName("course-button");
        addAttributeButton.addClickListener(click -> {

        });
        attributeLayout.add(addAttributeButton);

        courseDataLayout = new HorizontalLayout(courseDescrLayout, attributeLayout);
        courseDataLayout.setClassName("course-data-layout");
        courseDataLayout.setJustifyContentMode(JustifyContentMode.EVENLY);
        courseDataLayout.setAlignItems(Alignment.STRETCH);

        joinCourseButton = new Button("Join course");
        joinCourseButton.setClassName("big-button");
        joinCourseButton.addClickListener(click -> {

        });

        this.add(nameHeader, creationInfoLayout, courseDataLayout, joinCourseButton);

    }

}
