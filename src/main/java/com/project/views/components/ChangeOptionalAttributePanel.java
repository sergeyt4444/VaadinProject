package com.project.views.components;

import com.project.controller.ModeratorControllerInterface;
import com.project.controller.UserControllerInterface;
import com.project.entity.Obj;
import com.project.tools.AttributeTool;
import com.project.tools.ObjectConverter;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;

import java.util.Map;

public class ChangeOptionalAttributePanel extends FormLayout {

    private Select<String> select;
    private TextField attributeValueField;
    private Button submitButton;
    private Button closeDialog;

    public ChangeOptionalAttributePanel(UserControllerInterface controllerInterface,
                                        ModeratorControllerInterface moderatorControllerInterface,
                                        Obj obj, Dialog dialog) {

        Map<String, Integer> mappedObj = ObjectConverter.convertObjectToEtcAttrNames(obj);

        select = new Select<>();
        select.setLabel("Attribute name");
        select.setItems(mappedObj.keySet());

        attributeValueField = new TextField("Attribute value");

        submitButton = new Button("Change attribute");
        submitButton.addClickListener(click -> {
            if (select.getValue() == null) {
                Notification notification = new Notification("Illegal attribute name");
                notification.setPosition(Notification.Position.TOP_END);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.open();

            }
            else {
                Map<String, String> mappedObjAttr = AttributeTool.convertObjAttr(select.getValue(),
                        attributeValueField.getValue(), obj.getObjId());
                moderatorControllerInterface.changeObjAttr(mappedObjAttr);
                UI.getCurrent().getPage().reload();
                Notification notification = new Notification("Attribute has been changed");
                notification.setPosition(Notification.Position.TOP_END);
                notification.open();

            }

        });
        if (mappedObj.isEmpty()) {
            submitButton.setEnabled(false);
        }

        closeDialog = new Button("Exit");
        closeDialog.addClickListener(click -> {
            dialog.close();
        });

        this.setResponsiveSteps(new ResponsiveStep("0", 2));
        add(select, attributeValueField, submitButton, closeDialog);
    }

}
