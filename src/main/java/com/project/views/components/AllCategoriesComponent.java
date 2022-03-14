package com.project.views.components;

import com.project.entity.AttrEnum;
import com.project.views.CategoriesView;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.checkerframework.checker.units.qual.C;

import java.util.*;

public class AllCategoriesComponent extends VerticalLayout {

    private H2 categoriesHeader;
    private Map<Integer, List<CategoryNodeComponent>> mappedNodes;

    public AllCategoriesComponent(List<Map<Integer, String>> categories) {
        categoriesHeader = new H2("Categories");
        categoriesHeader.setClassName("categories-header");
        mappedNodes = new HashMap<>();
        for (Map<Integer, String> mappedCategory: categories) {
            int pid = 0;
            if (mappedCategory.containsKey(AttrEnum.PARENT_ID.getValue())) {
                pid = Integer.parseInt(mappedCategory.get(AttrEnum.PARENT_ID.getValue()));
            }
            if (!mappedNodes.containsKey(pid)) {
                mappedNodes.put(pid, new ArrayList<CategoryNodeComponent>());
            }
            mappedNodes.get(pid).add(new CategoryNodeComponent(mappedCategory));
        }

        for (CategoryNodeComponent node: mappedNodes.get(0)) {
            add(node);
            addNode(node);
        }
    }

    private void addNode(CategoryNodeComponent node) {
        if (mappedNodes.containsKey(node.getCategoryId())) {
            List<CategoryNodeComponent> children = mappedNodes.get(node.getCategoryId());
            for (CategoryNodeComponent child: children) {
                node.addChild(child);
                addNode(child);
            }
        }
    }

}
