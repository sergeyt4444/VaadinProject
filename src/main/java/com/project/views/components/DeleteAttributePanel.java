package com.project.views.components;

import com.project.controller.ModeratorControllerInterface;
import com.project.controller.UserControllerInterface;
import com.project.entity.Obj;
import com.project.tools.ObjectConverter;
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

    public DeleteAttributePanel(UserControllerInterface controllerInterface,
                                ModeratorControllerInterface moderatorControllerInterface,
                                Obj obj, Dialog dialog) {

        Map<String, Integer> mappedObj = ObjectConverter.convertObjectToEtcAttrNames(obj);

        select = new Select<>();
        select.setLabel("Attribute");
        select.setItems(mappedObj.keySet());

        submitButton = new Button("Delete attribute");
        submitButton.addClickListener(click -> {
            if (select.getValue() == null) {
                Notification notification = new Notification("Illegal attribute name");
                notification.setPosition(Notification.Position.TOP_END);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.open();
            }
            else {
                Map<String, Boolean> response = moderatorControllerInterface.deleteObjAttr(mappedObj.get(select.getValue()));
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
        this.setColspan(select, 2);
        add(select, submitButton, closeDialog);
    }

}
