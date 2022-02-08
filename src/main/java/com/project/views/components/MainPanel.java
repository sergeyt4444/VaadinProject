package com.project.views.components;

import com.project.entity.Obj;
import com.vaadin.flow.component.html.Div;
import java.util.List;

public class MainPanel extends Div {

    public MainPanel(List<Obj> list) {
        this.setClassName("main-panel");
        for (int i = 0; i < list.size(); i++) {
            add(new CourseCategoryPanel(list.get(i)));
        }
    }

}
