package com.project.views.components;

import com.project.entity.Obj;
import com.vaadin.flow.component.html.Div;
import java.util.List;
import java.util.Map;

public class MainPanel extends Div {

    public MainPanel(List<Map<Integer, String>> list) {
        this.setClassName("main-panel");
        for (int i = 0; i < list.size(); i++) {
            add(new CourseCategoryPanel(list.get(i)));
        }
    }

}
