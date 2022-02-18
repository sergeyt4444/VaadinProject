package com.project.views.components;


import com.project.controller.MainControllerInterface;
import com.project.entity.AttrEnum;
import com.project.entity.Obj;
import com.project.tools.ObjectConverter;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;

import java.util.List;
import java.util.Map;

public class SubcategoryMenu extends MenuBar {


    public SubcategoryMenu(MainControllerInterface controllerInterface, Map<Integer, String> rootMappedCategory, List<Map<Integer, String>> mappedObjList) {
        Icon icon = new Icon("angle-double-left");
        ComponentEventListener<ClickEvent<MenuItem>> returnListener = e -> {
            if (!rootMappedCategory.containsKey(AttrEnum.PARENT_ID.getValue()) || rootMappedCategory.get(AttrEnum.PARENT_ID.getValue()).equals("0")) {
                this.getUI().ifPresent(ui -> {
                    ui.navigate("vaadin_project/main_page");
                });
            }
            else {
                Obj parentCategory = controllerInterface.getObjectById(
                        Integer.parseInt(rootMappedCategory.get(AttrEnum.PARENT_ID.getValue()))).getBody();
                Map<Integer, String> mappedParentCat = ObjectConverter.convertObject(parentCategory);
                String parentCategoryName = mappedParentCat.get(AttrEnum.COURSE_NAME.getValue());
                this.getUI().ifPresent(ui -> {
                    ui.navigate("vaadin_project/" + parentCategoryName);
                });
            }
        };
        this.addItem(icon, returnListener);
        MenuItem currentCategoryItem = this.addItem(rootMappedCategory.get(AttrEnum.COURSE_NAME.getValue()));
        currentCategoryItem.setEnabled(false);

        ComponentEventListener<ClickEvent<MenuItem>> listener = e -> {
            this.getUI().ifPresent(ui -> {
                ui.navigate("vaadin_project/" + e.getSource().getText());
            });
        };

        for (Map<Integer, String> mappedObj: mappedObjList) {
            this.addItem(mappedObj.get(AttrEnum.COURSE_NAME.getValue()), listener);
        }
    }

}
