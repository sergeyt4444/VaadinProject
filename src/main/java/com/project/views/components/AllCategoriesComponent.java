package com.project.views.components;

import com.project.entity.AttrEnum;
import com.project.tools.ObjectConverter;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.QueryParameters;

import java.util.*;

public class AllCategoriesComponent extends VerticalLayout {

    private H2 categoriesHeader;
    private Map<Integer, List<Map<Integer,String>>> mappedNodes;
    private TreeGrid<Map<Integer, String>> treeGrid;

    public AllCategoriesComponent(List<Map<Integer, String>> categories) {
        setAlignItems(Alignment.CENTER);
        setSizeFull();

        categoriesHeader = new H2("Categories");
        categoriesHeader.setClassName("categories-header");
        mappedNodes = new HashMap<>();
        for (Map<Integer, String> mappedCategory: categories) {
            int pid = 0;
            if (mappedCategory.containsKey(AttrEnum.PARENT_ID.getValue())) {
                pid = Integer.parseInt(mappedCategory.get(AttrEnum.PARENT_ID.getValue()));
            }
            if (!mappedNodes.containsKey(pid)) {
                mappedNodes.put(pid, new ArrayList<Map<Integer,String>>());
            }
            mappedNodes.get(pid).add(mappedCategory);
        }

        treeGrid = new TreeGrid<>();
        treeGrid.setClassName("categories-grid");
        treeGrid.setHeight("500px");
        treeGrid.setItems(mappedNodes.get(0));
        Stack<Map<Integer,String>> treeStack = new Stack<>();
        for (Map<Integer, String> map: mappedNodes.get(0)) {
            treeStack.push(map);
        }
        while (!treeStack.isEmpty()) {
            Map<Integer, String> map = treeStack.pop();
            if (mappedNodes.get(ObjectConverter.getIdFromMappedObj(map)) != null) {
                treeGrid.getTreeData().addItems(map,
                        mappedNodes.get(ObjectConverter.getIdFromMappedObj(map)));
                for (Map<Integer, String> map2: mappedNodes.get(ObjectConverter.getIdFromMappedObj(map))) {
                    treeStack.push(map2);
                }
            }
        }

        treeGrid.addHierarchyColumn(map -> map.get(AttrEnum.COURSE_NAME.getValue())).setHeader("Category name")
                .setResizable(true).setWidth("400px");
        treeGrid.addColumn(map -> map.get(AttrEnum.COURSE_DESCRIPTION.getValue())).setHeader("Category description")
                .setResizable(true).setWidth("400px");

        treeGrid.addComponentColumn(map ->{
            Icon icon = new Icon("arrow-forward");
            Button goToCourse = new Button(icon);
            goToCourse.setClassName("goto-button");
            goToCourse.addClickListener(click -> {
                getUI().ifPresent(ui -> {
                    ui.navigate("vaadin_project/" + map.get(AttrEnum.COURSE_NAME.getValue()));
                });
            });
            return goToCourse;
        }).setHeader("Go to").setAutoWidth(true).setFlexGrow(0);

        treeGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        add(categoriesHeader, treeGrid);
    }

}
