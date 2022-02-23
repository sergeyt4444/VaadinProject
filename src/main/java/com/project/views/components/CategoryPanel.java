package com.project.views.components;

import com.project.controller.MainControllerInterface;
import com.project.entity.AttrEnum;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;
import java.util.Map;

public class CategoryPanel extends VerticalLayout {

    private SubcategoryMenu subcategoryMenu;
    private CourseFilterDetails courseFilterDetails;
    private Details filterDetails;
    private CoursesDiv coursesDiv;

    public CategoryPanel(MainControllerInterface controllerInterface, Map<Integer, String> rootMappedCategory,
                         List<Map<Integer, String>> mappedCategories, List<Map<Integer, String>> mappedCourses) {
        subcategoryMenu = new SubcategoryMenu(controllerInterface, rootMappedCategory, mappedCategories);
        courseFilterDetails = new CourseFilterDetails(rootMappedCategory.get(AttrEnum.COURSE_NAME.getValue()));
        filterDetails = new Details("Filter", courseFilterDetails);
        coursesDiv = new CoursesDiv(mappedCourses);
        add(subcategoryMenu, filterDetails, coursesDiv);
    }

}
