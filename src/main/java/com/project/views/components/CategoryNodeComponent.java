package com.project.views.components;

import com.project.entity.AttrEnum;
import com.project.tools.ObjectConverter;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.Map;

public class CategoryNodeComponent extends VerticalLayout {

    private Label nameLabel;
    private Label descrLabel;
    private Button gotoButton;
    private int pid;
    private int id;
    private Details children;
    private VerticalLayout childrenLayout;
    private HorizontalLayout contentLayout;

    public CategoryNodeComponent(Map<Integer, String> mappedCategory) {
        setClassName("category-node");

        nameLabel = new Label(mappedCategory.get(AttrEnum.COURSE_NAME.getValue()));
        nameLabel.setClassName("category-node-name-label");
        descrLabel = new Label(mappedCategory.get(AttrEnum.COURSE_DESCRIPTION.getValue()));
        descrLabel.setClassName("category-node-descr-label");
        Icon icon = new Icon("arrow-forward");
        gotoButton = new Button(icon);
        gotoButton.setClassName("goto-button");
        gotoButton.addClickListener(buttonClickEvent -> {
            getUI().ifPresent(ui -> {
                ui.navigate("vaadin_project/" + mappedCategory.get(AttrEnum.COURSE_NAME.getValue()));
            });
        });
        pid = 0;
        if (mappedCategory.containsKey(AttrEnum.PARENT_ID.getValue())) {
            pid = Integer.parseInt(mappedCategory.get(AttrEnum.PARENT_ID.getValue()));
        }
        id = ObjectConverter.getIdFromMappedObj(mappedCategory);
        childrenLayout = new VerticalLayout();
        childrenLayout.setClassName("subcategories-layout");
        children = new Details("Subcategories", childrenLayout);
        children.setClassName("subcategories-details");

        contentLayout = new HorizontalLayout(nameLabel, descrLabel, gotoButton);
        contentLayout.setClassName("category-content-layout");
        contentLayout.setAlignItems(Alignment.CENTER);
        contentLayout.setJustifyContentMode(JustifyContentMode.START);

        children.setVisible(false);

        add(contentLayout, children);
    }

    public int getPid() {
        return pid;
    }

    public int getCategoryId() {
        return id;
    }

    public void addChild(CategoryNodeComponent child) {
        childrenLayout.add(child);
        children.setVisible(true);
    }

}
