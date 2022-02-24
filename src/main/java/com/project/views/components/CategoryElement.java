package com.project.views.components;

import com.project.entity.AttrEnum;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.Map;



@CssImport("./styles/styles.css")
public class CategoryElement extends VerticalLayout {

    private Label categoryNameLabel;
    private Label categoryDescrLabel;

    public CategoryElement(Map<Integer, String> mappedObj) {
        addClassName("course-category-panel");
        categoryNameLabel = new Label();
        categoryNameLabel.addClassName("course-name-label");
        String courseName = mappedObj.get(AttrEnum.COURSE_NAME.getValue());
        String courseDescr = mappedObj.get(AttrEnum.COURSE_DESCRIPTION.getValue());
        categoryNameLabel.getElement().setProperty("innerHTML", "<b>" + courseName + "</b>");
        categoryDescrLabel = new Label(courseDescr);
        categoryDescrLabel.setClassName("course-description-label");

        this.setAlignItems(Alignment.STRETCH);
        addClickListener(click -> {
            getUI().ifPresent(ui -> {
                ui.navigate("vaadin_project/" + courseName);
            });
        });

        add(categoryNameLabel, categoryDescrLabel);
    }

}
