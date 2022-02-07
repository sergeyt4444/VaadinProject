package com.project.views.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;


public class NavPanel extends VerticalLayout {

    private Button mainPageButton;
    private Button catPageButton;
    private Button recentCourcesButton;
    private Button courseCalendarButton;
    private Button contactPageButton;

    public NavPanel() {
        getStyle().set("background-color", "#f0f0f0");
        setMinHeight("700px");

        mainPageButton = new Button("Главная страница");
        mainPageButton.addClickListener(click -> {
            System.out.println("goto mainpage");
        });
        catPageButton = new Button("Категории");
        catPageButton.addClickListener(click -> {
        });
        recentCourcesButton = new Button("Новые тренинги");
        recentCourcesButton.addClickListener(click -> {
        });
        courseCalendarButton = new Button("Запланированные курсы");
        courseCalendarButton.addClickListener(click -> {
        });
        contactPageButton = new Button("Обратная связь");
        contactPageButton.addClickListener(click -> {
        });

        add(mainPageButton, catPageButton, recentCourcesButton, courseCalendarButton, contactPageButton);
    }

}