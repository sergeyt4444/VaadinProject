package com.project.views.components;


import com.project.tools.AttributeTool;
import com.project.tools.ObjectConverter;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.QueryParameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CourseFilterDetails extends HorizontalLayout {

    private CheckboxGroup<String> difficultyGroup;
    private CheckboxGroup<String> languageGroup;
    private CheckboxGroup<String> formatGroup;
    private Button filterButton;

    public CourseFilterDetails(String courseName) {

        this.setAlignItems(Alignment.START);
        this.setJustifyContentMode(JustifyContentMode.CENTER);
        this.addClassName("course-filter-details");

        difficultyGroup = new CheckboxGroup<>();
        difficultyGroup.setLabel("Difficulties");
        difficultyGroup.setItems(AttributeTool.getDifficulties());
        difficultyGroup.select(AttributeTool.getDifficulties());
        difficultyGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        difficultyGroup.addClassName("filter-checkbox-group");

        languageGroup = new CheckboxGroup<>();
        languageGroup.setLabel("Languages");
        languageGroup.setItems(AttributeTool.getLanguages());
        languageGroup.select(AttributeTool.getLanguages());
        languageGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        languageGroup.addClassName("filter-checkbox-group");

        formatGroup = new CheckboxGroup<>();
        formatGroup.setLabel("Formats");
        formatGroup.setItems(AttributeTool.getFormats());
        formatGroup.select(AttributeTool.getFormats());
        formatGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        formatGroup.addClassName("filter-checkbox-group");

        filterButton = new Button("Filter");
        filterButton.setClassName("filter-button");
        filterButton.addClickListener(click -> {
            getUI().ifPresent(ui -> {
                Map<String, String[]> parameters = new HashMap<>();
                parameters.put("difficulty", difficultyGroup.getSelectedItems().toArray(new String[0]));
                parameters.put("language", languageGroup.getSelectedItems().toArray(new String[0]));
                parameters.put("format", formatGroup.getSelectedItems().toArray(new String[0]));
                ui.navigate("vaadin_project/"+courseName, QueryParameters.full(parameters));
            });
        });

        add(difficultyGroup, languageGroup, formatGroup, filterButton);

    }

}
