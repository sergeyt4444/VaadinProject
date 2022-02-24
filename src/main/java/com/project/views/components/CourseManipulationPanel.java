package com.project.views.components;

import com.project.tools.AttributeTool;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;

import java.time.LocalDate;

public class CourseManipulationPanel extends FormLayout {

    protected TextField courseNameField;
    protected TextField courseDescrField;
    protected IntegerField minParticipantsField;
    protected Select<String> difficultySelect;
    protected Select<String> langSelect;
    protected Select<String> formatSelect;
    protected DatePicker startDateDPicker;

    public CourseManipulationPanel() {
        this.setClassName("course-creation-panel");

        courseNameField = new TextField("Course name");
        courseNameField.addClassName("course-creation-tf");
        courseDescrField = new TextField("Course description");
        courseDescrField.addClassName("course-creation-tf");

        minParticipantsField = new IntegerField();
        minParticipantsField.setLabel("Required № of participants");
        minParticipantsField.setValue(10);
        minParticipantsField.setMin(1);
        minParticipantsField.setMax(250);
        minParticipantsField.addClassName("number-field");
        minParticipantsField.setHasControls(true);

        difficultySelect = new Select<>();
        difficultySelect.setItems(AttributeTool.getDifficulties());
        difficultySelect.setLabel("Difficulty");
        difficultySelect.setValue(AttributeTool.getDifficulties().get(0));
        difficultySelect.addClassName("primary-attr-label");

        langSelect = new Select<>();
        langSelect.setItems(AttributeTool.getLanguages());
        langSelect.setLabel("Language");
        langSelect.setValue(AttributeTool.getLanguages().get(0));
        langSelect.addClassName("primary-attr-label");

        formatSelect = new Select<>();
        formatSelect.setItems(AttributeTool.getFormats());
        formatSelect.setLabel("Format");
        formatSelect.setValue(AttributeTool.getFormats().get(0));
        formatSelect.addClassName("primary-attr-label");

        startDateDPicker = new com.vaadin.flow.component.datepicker.DatePicker();
        startDateDPicker.setLabel("Start date");
        startDateDPicker.setValue(LocalDate.now());
        startDateDPicker.setMin(LocalDate.now());
        startDateDPicker.setMax(LocalDate.now().plusYears(4));
        startDateDPicker.addClassName("primary-attr-dpicker");


        this.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 6));
        this.setColspan(courseNameField, 6);
        this.setColspan(courseDescrField, 6);
        this.setColspan(minParticipantsField, 6);
        this.setColspan(difficultySelect, 3);
        this.setColspan(langSelect, 3);
        this.setColspan(formatSelect, 3);
        this.setColspan(startDateDPicker, 3);
        add(courseNameField, courseDescrField, minParticipantsField, difficultySelect,
                langSelect, formatSelect, startDateDPicker);
    }


}