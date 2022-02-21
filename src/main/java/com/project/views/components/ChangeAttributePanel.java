package com.project.views.components;

import com.project.controller.MainControllerInterface;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.select.Select;

public class ChangeAttributePanel extends FormLayout {

    private Select<String> attributeSelect;
    private Select<String> getAttributeSelect;
    private Button submitButton;
    private Button closeDialogButton;

    public ChangeAttributePanel(MainControllerInterface controllerInterface, Dialog dialog) {

        attributeSelect = new Select<>();
        attributeSelect.setItems("");

    }

}
