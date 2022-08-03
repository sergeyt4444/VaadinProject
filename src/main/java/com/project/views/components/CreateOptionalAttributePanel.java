package com.project.views.components;

import com.project.controller.ModeratorControllerInterface;
import com.project.controller.UserControllerInterface;
import com.project.entity.Obj;
import com.project.entity.ObjAttr;
import com.project.tools.AttributeTool;
import com.project.tools.ObjectConverter;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextField;

import java.util.Map;

public class CreateOptionalAttributePanel extends FormLayout {

    private TextField attrNameField;
    private TextField attrValueField;
    private Button submitButton;
    private Button closeDialog;

    public CreateOptionalAttributePanel(UserControllerInterface controllerInterface,
                                        ModeratorControllerInterface moderatorControllerInterface,
                                        Map<Integer, String> mappedObj, Dialog dialog) {
        attrNameField = new TextField("Attribute name");

        attrValueField = new TextField("Attribute value");

        submitButton = new Button("Create attribute");
        submitButton.addClickListener(click -> {
            Obj obj = controllerInterface.getObjectById(ObjectConverter.getIdFromMappedObj(mappedObj)).getBody();
            if (obj != null) {
                boolean contains = false;
                boolean isOptional = true;
                for (ObjAttr objAttr: obj.getObjAttrs()) {
                    if (objAttr.getAttribute().getAttrName().equals(attrNameField.getValue())) {
                        contains = true;
                        if (objAttr.getAttribute().getAttrId() <= AttributeTool.PRIMARY_ATTRIBUTE_ID_SPACE) {
                            isOptional = false;
                        }
                    }
                }
                if (isOptional && !contains) {
                    Map<String, String> mappedObjAttr = AttributeTool.convertObjAttr(attrNameField.getValue(),
                            attrValueField.getValue(), ObjectConverter.getIdFromMappedObj(mappedObj));
                    moderatorControllerInterface.createObjAttr(mappedObjAttr);
                    UI.getCurrent().getPage().reload();
                    Notification notification = new Notification("Attribute has been added");
                    notification.setPosition(Notification.Position.TOP_END);
                    notification.open();
                }
                else {
                    if (isOptional) {
                        Notification notification = new Notification("To alter optional attribute, " +
                                "use button \"Change optional attribute\"");
                        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                        notification.setPosition(Notification.Position.TOP_END);
                        notification.open();
                    }
                    else {
                        Notification notification = new Notification("To alter primary attribute, " +
                                "use button \"Change primary attribute\"");
                        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                        notification.setPosition(Notification.Position.TOP_END);
                        notification.open();
                    }
                }
            }
            UI.getCurrent().getPage().reload();
        });

        closeDialog = new Button("Exit");
        closeDialog.addClickListener(click -> {
            dialog.close();
        });

        this.setResponsiveSteps(new ResponsiveStep("0", 2));
        add(attrNameField, attrValueField, submitButton, closeDialog);

    }

}
