package com.project.views.components;

import com.vaadin.flow.component.html.Div;
import java.util.List;
import java.util.Map;

public class CategoriesDiv extends Div {

    public CategoriesDiv(List<Map<Integer, String>> list) {
        this.setClassName("main-panel");
        for (int i = 0; i < list.size(); i++) {
            add(new CategoryElement(list.get(i)));
        }
    }

}
