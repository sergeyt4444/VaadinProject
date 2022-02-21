package com.project.views.components;

import com.project.controller.MainControllerInterface;
import com.project.entity.Obj;
import com.project.entity.ObjAttr;
import com.project.tools.AttributeTool;
import com.project.tools.ObjectConverter;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.select.Select;

import java.util.Map;

public class DeleteAttributePanel extends FormLayout {

    private Select<String> select;
    private Button submitButton;
    private Button closeDialog;

    public DeleteAttributePanel(MainControllerInterface controllerInterface,
                                Obj obj, Dialog dialog) {

        Map<String, Integer> mappedObj = ObjectConverter.convertObjectToEtcAttrNames(obj);

        select = new Select<>();
        select.setLabel("Attribute");
        select.setItems(mappedObj.keySet());

        submitButton = new Button("Delete attribute");
        submitButton.addClickListener(click -> {
            Map<String, Boolean> response = controllerInterface.deleteObjAttr(mappedObj.get(select.getValue()));
            if (response.get("deleted") == true) {
                UI.getCurrent().getPage().reload();
                Notification notification = new Notification("Attribute has been deleted");
                notification.setPosition(Notification.Position.TOP_END);
                notification.open();
            }
            else {
                Notification notification = new Notification("Error");
                notification.setPosition(Notification.Position.TOP_END);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.open();

            }
        });

        closeDialog = new Button("Exit");
        closeDialog.addClickListener(click -> {
            dialog.close();
        });

        this.setResponsiveSteps(new ResponsiveStep("0", 2));
        this.setColspan(select, 2);
        add(select, submitButton, closeDialog);
    }

}
