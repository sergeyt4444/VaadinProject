package com.project.views.components;

import com.vaadin.flow.component.html.Div;

import java.util.List;
import java.util.Map;

public class CoursesDiv extends Div {

    public CoursesDiv(List<Map<Integer, String>> list) {
        this.setClassName("courses-div");
        for (int i = 0; i < list.size(); i++) {
            add(new CourseElement(list.get(i)));
        }
    }

}
