package com.project.views.components;

import com.project.controller.MainControllerInterface;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;
import java.util.Map;

public class CategoryPanel extends VerticalLayout {

    private SubcategoryMenu subcategoryMenu;
    private CoursesDiv coursesDiv;

    public CategoryPanel(MainControllerInterface controllerInterface, Map<Integer, String> rootMappedCategory, List<Map<Integer, String>> mappedCategories, List<Map<Integer, String>> mappedCourses) {
        subcategoryMenu = new SubcategoryMenu(controllerInterface, rootMappedCategory, mappedCategories);
        coursesDiv = new CoursesDiv(mappedCourses);
        add(subcategoryMenu, coursesDiv);
    }

}
