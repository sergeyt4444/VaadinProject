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
        this.setClassName("nav-panel");

        mainPageButton = new Button("Главная страница");
        mainPageButton.addClickListener(click -> {
            System.out.println("goto mainpage");
        });
        mainPageButton.setClassName("nav-button");
        catPageButton = new Button("Категории");
        catPageButton.addClickListener(click -> {
        });
        catPageButton.setClassName("nav-button");
        recentCourcesButton = new Button("Новые тренинги");
        recentCourcesButton.addClickListener(click -> {
        });
        recentCourcesButton.setClassName("nav-button");
        courseCalendarButton = new Button("Запланированные курсы");
        courseCalendarButton.addClickListener(click -> {
        });
        courseCalendarButton.setClassName("nav-button");
        contactPageButton = new Button("Обратная связь");
        contactPageButton.addClickListener(click -> {
        });
        contactPageButton.setClassName("nav-button");

        add(mainPageButton, catPageButton, recentCourcesButton, courseCalendarButton, contactPageButton);
    }

}