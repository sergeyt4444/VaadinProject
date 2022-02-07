package com.project.views.components;

import com.project.entity.Obj;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainPanel extends VerticalLayout {

    private VerticalLayout rows;

    public MainPanel(List<Obj> list) {
        this.setMinWidth("90%");
        this.setMinHeight("80%");
        rows = new VerticalLayout();
        rows.setAlignItems(Alignment.CENTER);
        rows.setJustifyContentMode(JustifyContentMode.CENTER);
        int rowNum = list.size()/3 + 1;
        for (int i = 0; i < rowNum; i++) {
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.setAlignItems(Alignment.CENTER);
            horizontalLayout.setJustifyContentMode(JustifyContentMode.EVENLY);
            for (int j = 0; j < 3; j++) {
                if (i*3+j < list.size())
                    horizontalLayout.add(new CourseCategoryPanel(list.get(i*3 + j)));
            }
            rows.add(horizontalLayout);
        }
        add(rows);
    }

}
