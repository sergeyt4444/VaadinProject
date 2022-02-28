package com.project.views.components;

import com.project.controller.MainControllerInterface;
import com.project.entity.AttrEnum;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;

import java.util.List;
import java.util.Map;

public class CategoryPanel extends VerticalLayout {

    private SubcategoryMenu subcategoryMenu;
    private CourseFilterDetails courseFilterDetails;
    private CoursesDiv coursesDiv;
    private HorizontalLayout pageNavLayout;
    private PageNavigationComponent pageNavigationComponent;

    public CategoryPanel(MainControllerInterface controllerInterface, Map<Integer, String> rootMappedCategory,
                         List<Map<Integer, String>> mappedCategories, List<Map<Integer, String>> mappedCourses,
                         Map<String, List<String>> queryParamList, int currentPage, int pagesCount, BeforeEvent event) {
        subcategoryMenu = new SubcategoryMenu(controllerInterface, rootMappedCategory, mappedCategories);
        courseFilterDetails = new CourseFilterDetails(rootMappedCategory.get(AttrEnum.COURSE_NAME.getValue()), queryParamList);
        coursesDiv = new CoursesDiv(mappedCourses);
        pageNavigationComponent = new PageNavigationComponent(currentPage, pagesCount, event);
        pageNavLayout = new HorizontalLayout(pageNavigationComponent);
        pageNavLayout.setAlignItems(Alignment.CENTER);
        pageNavLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        pageNavLayout.setClassName("page-nav-layout");
        add(subcategoryMenu, courseFilterDetails, coursesDiv, pageNavLayout);
    }

}
